package com.example.rest_security_api.service;

import com.example.rest_security_api.dto.FileReadDto;
import com.example.rest_security_api.entity.Event;
import com.example.rest_security_api.entity.File;
import com.example.rest_security_api.entity.Status;
import com.example.rest_security_api.entity.User;
import com.example.rest_security_api.mapper.FileReadMapper;
import com.example.rest_security_api.repository.FileRepository;
import com.example.rest_security_api.util.GetLocationFile;
import com.example.rest_security_api.util.EventUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    public static String BUCKET_NAME = "timurul112bucket";

    private final S3Service s3Service;
    private final FileRepository fileRepository;
    private final FileReadMapper fileReadMapper;

    private final EventService eventService;

    private final UserService userService;

    private final EventUserUtil eventUserUtil;


    public Optional<FileReadDto> getById(Integer fileId) {
        Optional<File> optionalFile = fileRepository.findById(fileId);
        if (optionalFile.isEmpty()) {
            throw new RuntimeException("Record not found");
        } else {
            return Optional.of(fileReadMapper.mapToDto(optionalFile.get()));
        }
    }

    public List<String> getAll() {
        return s3Service.getListFiles();
    }

    @Transactional
    public File saveFileInDataBase(String username, String fileName) {
        String location = GetLocationFile.getLocation(BUCKET_NAME, fileName);
        File savedFile = File.builder()
                .createdBy(username)
                .status(Status.ACTIVE)
                .name(fileName)
                .location(location)
                .build();
        return fileRepository.save(savedFile);
    }


    @Transactional
    public void uploadFile(String fileName, String fileContent, String username) {
        s3Service.uploadFile(BUCKET_NAME, fileName, fileContent);
        Event event = eventUserUtil.getEventAndUpdateFileKey(username, fileName);
        eventService.save(event);
    }


    @Transactional
    public void deleteOwnFile(String fileName, String username) {
        File file = fileRepository.getByName(fileName).orElseThrow(() -> new RuntimeException("File does not exist"));
        if (!file.getCreatedBy().equals(username)) {
            throw new RuntimeException("No access to file");
        }
        s3Service.deleteFile(BUCKET_NAME, fileName);
        file.setStatus(Status.DELETED);
        Event event = eventUserUtil.getEventAndUpdateFileKey(file, username);
        eventService.save(event);
    }

    public void deleteByName(String fileName, String username) {


    }
//
//        User user = userService.getByUsername(username);
//        if (!user.getFileKeys().contains(fileName)) {
//            throw new RuntimeException("No access to file");
//        }
//        s3Service.deleteFile(BUCKET_NAME, fileName);
//        Event event = eventUtil.getEvent(fileName, user);
//        user.getFileKeys().add(fileName);
//        eventService.save(event);
//    }
}

package com.example.rest_security_api.service;

import com.example.rest_security_api.dto.FileReadDto;
import com.example.rest_security_api.entity.Event;
import com.example.rest_security_api.entity.File;
import com.example.rest_security_api.entity.Status;
import com.example.rest_security_api.mapper.FileReadMapper;
import com.example.rest_security_api.repository.FileRepository;
import com.example.rest_security_api.util.EventUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
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
    public void uploadFileInS3(String fileName, String fileContent, String username) {
        Event event = eventUserUtil.getEventAndUpdateFileKey(username, fileName);
        eventService.save(event);
        s3Service.uploadFile(BUCKET_NAME, fileName, fileContent);
    }


    @Transactional
    public void deleteOwnFile(String fileName, String username) {
        File file = fileRepository.getByName(fileName).orElseThrow(() -> new RuntimeException("File does not exist"));
        if (!file.getCreatedBy().equals(username)) {
            throw new RuntimeException("No access to file");
        }
        file.setStatus(Status.DELETED);
        Event event = eventUserUtil.getEventAndUpdateFileKey(file, username);
        eventService.save(event);
        s3Service.deleteFile(BUCKET_NAME, fileName);
    }

    @Transactional
    public void deleteFileByName(String fileName, String username) {
        File file = fileRepository.getByName(fileName).orElseThrow(() -> new RuntimeException("File does not exist"));
        Event event = eventUserUtil.getEventAndUpdateFileKey(file, username);
        eventService.save(event);
        s3Service.deleteFile(BUCKET_NAME, fileName);
    }

    @Transactional
    public String downloadOwnFile(String username, String fileName) throws IOException {
        File file = fileRepository.getByName(fileName).orElseThrow(() -> new RuntimeException("File does not exist"));
        if (!file.getCreatedBy().equals(fileName)) {
            throw new AccessDeniedException("No access to file");
        }
        Event event = eventUserUtil.getEvent(file, username);
        eventService.save(event);
        return s3Service.downloadFile(fileName);
    }

    @Transactional
    public String downloadFileByName(String username, String fileName) throws IOException {
        File file = fileRepository.getByName(fileName).orElseThrow(() -> new RuntimeException("File does not exist"));
        Event event = eventUserUtil.getEvent(file, username);
        eventService.save(event);
        return s3Service.downloadFile(fileName);
    }
}

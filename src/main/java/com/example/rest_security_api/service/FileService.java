package com.example.rest_security_api.service;

import com.example.rest_security_api.dto.FileReadDto;
import com.example.rest_security_api.entity.File;
import com.example.rest_security_api.entity.Status;
import com.example.rest_security_api.mapper.FileReadMapper;
import com.example.rest_security_api.repository.FileRepository;
import com.example.rest_security_api.util.GetLocationFile;
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


    public Optional<FileReadDto> getById(Integer fileId) {
        Optional<File> optionalFile = fileRepository.findById(fileId);
        if (optionalFile.isEmpty()) {
            throw new RuntimeException("Record not found");
        } else {
            return Optional.of(fileReadMapper.map(optionalFile.get()));
        }
    }

    public List<String> getAll() {
        return s3Service.getListFiles();
    }

    public void saveFileInDataBase(String username, String fileName) {
        String location = GetLocationFile.getLocation(BUCKET_NAME, fileName);
        File savedFile = File.builder()
                .createdBy(username)
                .status(Status.ACTIVE)
                .name(fileName)
                .location(location)
                .build();
        fileRepository.save(savedFile);
    }



    public void uploadFile(String key, String fileContent, String username) {
        s3Service.uploadFile(BUCKET_NAME, key, fileContent);
        saveFileInDataBase(username, key);
        saveEventInDataBase();

    }


}

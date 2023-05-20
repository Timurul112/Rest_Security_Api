package com.example.rest_security_api.service;

import com.amazonaws.services.s3.model.Bucket;
import com.example.rest_security_api.dto.FileDto;
import com.example.rest_security_api.entity.Event;
import com.example.rest_security_api.entity.File;
import com.example.rest_security_api.entity.Status;
import com.example.rest_security_api.mapper.FileReadMapper;
import com.example.rest_security_api.repository.FileRepository;
import com.example.rest_security_api.util.EventUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {




    private final S3Service s3Service;
    private final FileRepository fileRepository;
    private final FileReadMapper fileReadMapper;

    private final EventService eventService;

    private final EventUserUtil eventUserUtil;


    public Optional<FileDto> getById(Integer fileId) {
        Optional<File> optionalFile = fileRepository.findById(fileId);
        if (optionalFile.isEmpty()) {
            throw new RuntimeException("Record not found");
        } else {
            return Optional.of(fileReadMapper.mapToDto(optionalFile.get()));
        }
    }

    public List<FileDto> getAll(String bucketName) {
        return s3Service.getListFiles(bucketName);
    }


    @Transactional
    public void uploadFileInS3(String fileName, MultipartFile file, String username, String bucketName) {
        List<String> listNameBuckets = s3Service.getListBuckets().stream().map(Bucket::getName).toList();
        if (!listNameBuckets.contains(bucketName)) {
            s3Service.createBucket(bucketName);
        }
        Event event = eventUserUtil.getEventForUpload(username, fileName, bucketName);
        eventService.save(event);
        s3Service.uploadFile(bucketName, fileName, file);
    }


    @Transactional
    public void deleteOwnFile(String fileName, String username, String bucketName) {
        File file = fileRepository.getByName(fileName).orElseThrow(() -> new RuntimeException("File does not exist"));
        if (!file.getCreatedBy().equals(username)) {
            throw new RuntimeException("No access to file");
        }
        file.setStatus(Status.DELETED);
        Event event = eventUserUtil.getEventForDelete(file, username);
        eventService.save(event);
        s3Service.deleteFile(bucketName, fileName);
    }

    @Transactional
    public void deleteFileByName(String fileName, String username, String bucketName) {
        File file = fileRepository.getByName(fileName).orElseThrow(() -> new RuntimeException("File does not exist"));
        file.setStatus(Status.DELETED);
        Event event = eventUserUtil.getEventForDelete(file, username);
        eventService.save(event);
        s3Service.deleteFile(bucketName, fileName);
    }

    @Transactional
    public String downloadOwnFile(String username, String fileName, String bucketName) throws IOException {
        File file = fileRepository.getByName(fileName).orElseThrow(() -> new RuntimeException("File does not exist"));
        if (!file.getCreatedBy().equals(username)) {
            throw new AccessDeniedException("No access to file");
        }
        Event event = eventUserUtil.getEventForDownloadFile(file, username);
        eventService.save(event);
        return s3Service.downloadFile(fileName, bucketName);
    }

    @Transactional
    public String downloadFileByName(String username, String fileName, String bucketName) throws IOException {
        File file = fileRepository.getByName(fileName).orElseThrow(() -> new RuntimeException("File does not exist"));
        Event event = eventUserUtil.getEventForDownloadFile(file, username);
        eventService.save(event);
        return s3Service.downloadFile(fileName, bucketName);
    }


    @Transactional
    public void updateOwnFile(MultipartFile file, String username, String fileName, String bucketName) {
        File fileDB = fileRepository.getByName(fileName).orElseThrow(() -> new RuntimeException("File does not exist"));
        if (!fileDB.getCreatedBy().equals(username)) {
            throw new RuntimeException("No access to file");
        }
        Event event = eventUserUtil.getEventForUpdateFile(fileDB, username);
        eventService.save(event);
        s3Service.uploadFile(bucketName, fileName, file);
    }

    @Transactional
    public void updateFileByName(MultipartFile file, String username, String fileName, String bucketName) {
        File fileDB = fileRepository.getByName(fileName).orElseThrow(() -> new RuntimeException("File does not exist"));
        Event event = eventUserUtil.getEventForUpdateFile(fileDB, username);
        eventService.save(event);
        s3Service.uploadFile(bucketName, fileName, file);
    }
}

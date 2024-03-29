package com.example.rest_security_api.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.example.rest_security_api.dto.FileDto;
import com.example.rest_security_api.entity.File;
import com.example.rest_security_api.entity.Status;
import com.example.rest_security_api.repository.FileRepository;
import com.example.rest_security_api.util.GetLocationFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3client;

    private final FileRepository fileRepository;





    public void createBucket(String bucketName) {
        if (s3client.doesBucketExistV2(bucketName)) {
            throw new RuntimeException("Bucket already exists");
        }
        s3client.createBucket(bucketName);
    }

    public List<Bucket> getListBuckets() { //для админа
        return s3client.listBuckets();
    }

    public void uploadFile(String bucketName, String fileName, MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            ByteArrayInputStream content = new ByteArrayInputStream(bytes);
            s3client.putObject(bucketName, fileName, content, null);
        } catch (IOException e) {
            e.printStackTrace();
            new ResponseEntity<>("Failed to read file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<FileDto> getListFiles(String bucketName) { //для админа
        ObjectListing objects = s3client.listObjects(bucketName);
        ArrayList<FileDto> result = new ArrayList<>();
        for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
            String key = objectSummary.getKey();
            String location = GetLocationFileUtil.getLocation(bucketName, key);
            Optional<File> optionFile = fileRepository.getByName(key);
            if (optionFile.isEmpty()) {
                throw new RuntimeException("File does not exist");
            }
            File file = optionFile.get();
            String createdBy = file.getCreatedBy();
            Status status = file.getStatus();
            FileDto addFileDto = FileDto.builder()
                    .location(location)
                    .name(key)
                    .status(status)
                    .createdBy(createdBy).build();
            result.add(addFileDto);
        }
        return result;
    }


    public String downloadFile(String key, String bucketName) throws IOException {
        S3Object object = s3client.getObject(bucketName, key);
        S3ObjectInputStream objectContent = object.getObjectContent();
        return Arrays.toString(IOUtils.toByteArray(objectContent));
    }

    public void deleteFile(String bucketName, String key) {
        s3client.deleteObject(bucketName, key);
    }
}

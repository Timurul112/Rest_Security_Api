package com.example.rest_security_api.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3client;
    public static String BUCKET_NAME = "timurul112bucket";

    public void createBucket() {

        if (s3client.doesBucketExistV2(BUCKET_NAME)) {
            throw new RuntimeException("Bucket already exists");
        }
        s3client.createBucket(BUCKET_NAME);
    }

    public List<Bucket> getListBuckets() { //для админа
        return s3client.listBuckets();
    }

    public void uploadFile(String BUCKET_NAME, String fileName, String content) { //для всех
        s3client.putObject(BUCKET_NAME, fileName, content);
    }

    public List<String> getListFiles() { //для админа
        ObjectListing objects = s3client.listObjects(BUCKET_NAME);
        ArrayList<String> result = new ArrayList<>();
        for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
            String bucketName = objectSummary.getBucketName();
            String key = objectSummary.getKey();
            String appendString = "Bucket name- " + bucketName + ", Key name-" + key;
            result.add(appendString);
        }
        return result;
    }


    public String downloadFile(String key) throws IOException {
        S3Object object = s3client.getObject(BUCKET_NAME, key);
        S3ObjectInputStream objectContent = object.getObjectContent();
        return IOUtils.toByteArray(objectContent).toString();
    }

    public String getFileMetadata(String key) {
        ObjectMetadata objectMetadata = s3client.getObjectMetadata(BUCKET_NAME, key);
        long fileSize = objectMetadata.getContentLength();
        String lastDateModified = objectMetadata.getLastModified().toString();
        return "File size: " + fileSize + ", last date modified: " + lastDateModified + ", key name: " + key;
    }




    public void deleteFile(String BUCKET_NAME, String key) {
        s3client.deleteObject(BUCKET_NAME, key);
    }


}

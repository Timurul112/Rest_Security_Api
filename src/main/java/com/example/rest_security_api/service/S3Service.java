package com.example.rest_security_api.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3client;


    public void createBucket(String bucketName) {
        if (s3client.doesBucketExistV2(bucketName)) {
            throw new RuntimeException("Bucket already exists");
        }
        s3client.createBucket(bucketName);
    }

    public List<Bucket> getListBuckets() { //для админа
        return s3client.listBuckets();
    }

    public void uploadFile(String bucketName, String fileName, String content) {
        s3client.putObject(bucketName, fileName, content);
    }

    public List<String> getListFiles(String bucketName) { //для админа
        ObjectListing objects = s3client.listObjects(bucketName);
        ArrayList<String> result = new ArrayList<>();
        for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
            String key = objectSummary.getKey();
            String appendString = "Bucket name- " + bucketName + ", Key name-" + key;
            result.add(appendString);
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

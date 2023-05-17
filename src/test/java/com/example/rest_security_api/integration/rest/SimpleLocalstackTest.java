package com.example.rest_security_api.integration.rest;

import com.example.rest_security_api.container.initialization.Localstack;
import com.example.rest_security_api.integration.annotation.IT;
import com.example.rest_security_api.service.S3Service;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@IT
public class SimpleLocalstackTest {


    public static String BUCKET_NAME = "pes123";


    @Autowired
    private S3Service s3Service;
    @Autowired
    private ApplicationContext applicationContext;


    @BeforeAll
    static void init() {
        Localstack.container.start();
    }


    @Test
    void createBucketCheck() {
        s3Service.createBucket();
    }





    @Test
    void workCheckLocalstack() {
        String fileName = "file_name";
        String content = "test_content";
        s3Service.uploadFile(BUCKET_NAME, fileName, content);
    }


    @AfterAll
    static void stop() {
        Localstack.container.stop();
    }


}

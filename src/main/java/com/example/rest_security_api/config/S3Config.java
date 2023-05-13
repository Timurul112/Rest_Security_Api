package com.example.rest_security_api.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {


    @Bean
    public AmazonS3 s3client() {
        AWSCredentials credentials = new BasicAWSCredentials(
                "AKIASL3ARR6V7T4ULOUC",
                "Gh8ogaIeCLYBQdCV+hJuhC3oA/0ZnOtPJUr3VlKs"
        );

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }
}

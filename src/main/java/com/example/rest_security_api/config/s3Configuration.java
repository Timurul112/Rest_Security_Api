package com.example.rest_security_api.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class s3Configuration {

    @Value("${localstack.port}")
    private int hostPort;

    @Bean
    public AmazonS3 s3client() {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new EndpointConfiguration("http://localhost:%s".formatted(hostPort), Regions.EU_CENTRAL_1.getName()))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("accesskey", "secretkey")))
                .withPathStyleAccessEnabled(true)
                .build();
    }
}

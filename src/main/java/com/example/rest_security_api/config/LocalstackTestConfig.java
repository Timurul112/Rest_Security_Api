package com.example.rest_security_api.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class LocalstackTestConfig {

    @Value("${localstack.port}")
    private int hostPort;
//    private static final String LOCALSTACK_ENDPOINT = "http://localhost:%s".formatted();


    //    @Bean
//    public AmazonS3 s3client() {
//        AWSCredentials credentials = new BasicAWSCredentials(
//                "accessKey",
//                "secretKey"
//        );
//        return AmazonS3ClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:%s".formatted(hostPort)
//                        , "us-east-1"))
//                .build();
//    }
//}
    @Bean
    public AmazonS3 s3client() {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration("http://localhost:%s".formatted(hostPort),
                "us-east-1");
        AWSCredentials credentials = new BasicAWSCredentials(
                "accessKey",
                "secretKey"
        );
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(endpointConfiguration).build();
    }

}

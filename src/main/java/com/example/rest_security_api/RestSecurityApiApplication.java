package com.example.rest_security_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RestSecurityApiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RestSecurityApiApplication.class, args);
        System.out.println();
    }
}
package com.example.rest_security_api.integration.rest;

import com.example.rest_security_api.container.initialization.Localstack;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@TestPropertySource("classpath:application-test.properties")
class FileRestControllerV1IT {

    @BeforeAll
    static void init() {
        Localstack.container.start();
    }


    @Test
    void getAll() {


    }

    @Test
    void getById() {
    }

    @Test
    void upload() {
    }

    @Test
    void deleteByName() {
    }

    @Test
    void downloadFile() {
    }

    @Test
    void updateFile() {
    }

    @AfterAll
    static void stop() {
        Localstack.container.stop();
    }
}




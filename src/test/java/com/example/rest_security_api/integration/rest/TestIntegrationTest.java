package com.example.rest_security_api.integration.rest;

import com.example.rest_security_api.container.initialization.Minio;
import com.example.rest_security_api.container.initialization.Postgres;
import com.example.rest_security_api.dto.UserCreateDto;
import com.example.rest_security_api.dto.UserReadDto;
import com.example.rest_security_api.integration.annotation.IT;
import com.example.rest_security_api.service.S3Service;
import com.example.rest_security_api.service.UserService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

@IT
@ContextConfiguration(initializers = {Minio.Initializer.class, Postgres.Initialization.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestIntegrationTest {


    public static String BUCKET_NAME = "timurultest";


    @Autowired
    private S3Service s3Service;

    @Autowired
    private UserService userService;


    @BeforeAll
    static void init() {
        Minio.container.start();
        Postgres.container.start();


        Flyway flyway = Flyway.configure()
                .dataSource(Postgres.container.getJdbcUrl(), Postgres.container.getUsername(), Postgres.container.getPassword())
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
    }


    @Test
    @Order(1)
    void createBucketCheck() {
        s3Service.createBucket(BUCKET_NAME);
    }


    @Test
    @Order(2)
    void checkUploadFile() {
        String fileName = "file_name";
        byte[] contentByte = "upload_test".getBytes();
        MockMultipartFile multiFile = new MockMultipartFile(fileName, contentByte);
        s3Service.uploadFile(BUCKET_NAME, fileName, multiFile);
    }

    @Test
    @Order(3)
    void checkCreateUser() {
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .username("Timur")
                .rawPassword("123")
                .build();

        userService.create(userCreateDto);
    }

    @Test
    @Order(4)
    void checkGetByIdUser() {
        Integer userId = 1;
        Optional<UserReadDto> optionalUserReadDto = userService.getById(userId);
        optionalUserReadDto.orElseThrow(() -> new RuntimeException("User does not exist"));
    }


    @AfterAll
    static void stop() {
        Minio.container.stop();
        Postgres.container.stop();
    }
}

package com.example.rest_security_api.integration.rest;

import com.example.rest_security_api.container.initialization.Minio;
import com.example.rest_security_api.container.initialization.Postgres;
import com.example.rest_security_api.integration.annotation.IT;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(initializers = Minio.Initializer.class)
class FileRestControllerV1IT {






    @Autowired
    private MockMvc mockMvc;


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
    @WithMockUser(username = "Евгений", password = "1234",  authorities = "ADMIN")
    @Order(1)
    void upload() throws Exception {
        String fileContent = "testing";
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fileContent)
                        .param("fileName", "test1")
                        .param("bucketName", "timurul"))
                .andExpect(status().isCreated());
    }

//    @Test
//    @WithMockUser(username = "Евгений",password = "1234", authorities = "ADMIN")
//    void getAll() throws Exception {
//        File file = File.builder()
//                .id(1)
//                .status(Status.ACTIVE)
//                .name("test1")
//                .createdBy("user")
//                .location("location")
//                .build();
//        fileRepository.save(file);
//        fileRepository.flush();
//
//        mockMvc.perform(get("/api/v1/files/1"))
//                .andExpect(status().is2xxSuccessful());
//    }

    @Test
    void getById() {
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
        Minio.container.stop();
        Postgres.container.stop();
    }
}




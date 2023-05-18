package com.example.rest_security_api.integration.rest;

import com.example.rest_security_api.container.initialization.Minio;
import com.example.rest_security_api.container.initialization.Postgres;
import com.example.rest_security_api.dto.ContentDto;
import com.example.rest_security_api.dto.UserCreateDto;
import com.example.rest_security_api.integration.annotation.IT;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(initializers = {Minio.Initializer.class, Postgres.Initialization.class})
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
    @WithMockUser(username = "Timur", password = "1234", authorities = "ADMIN") //доделать
    @Order(1)
    void upload() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        UserCreateDto userCreateDto = UserCreateDto.builder()
                .username("Timur")
                .rawPassword("123")
                .build();
        String jsonRequestBodyTimur = objectMapper.writeValueAsString(userCreateDto);


        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBodyTimur));


        UserCreateDto userCreateDtoArtem = UserCreateDto.builder()
                .username("Artem")
                .rawPassword("123")
                .build();
        String jsonRequestBodyArtem = objectMapper.writeValueAsString(userCreateDtoArtem);


        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestBodyArtem));





        ContentDto contentDto = new ContentDto("content");
        String jsonRequestBodyContent = objectMapper.writeValueAsString(contentDto);

        mockMvc.perform(post("/api/v1/files")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBodyContent)
                        .param("fileName", "test1")
                        .param("bucketName", "timurul"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/files")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBodyContent)
                        .param("fileName", "test2")
                        .param("bucketName", "timurul"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "Евгений", password = "1234", authorities = "ADMIN")
    void getAll() throws Exception {
        System.out.println("asdasd");
        mockMvc.perform(get("/api/v1/files/1")
                        .param("bucketName", "timurul"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "Евгений", password = "1234", authorities = "ADMIN")
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




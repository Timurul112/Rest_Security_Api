package com.example.rest_security_api.integration.rest;

import com.example.rest_security_api.container.initialization.Minio;
import com.example.rest_security_api.container.initialization.Postgres;
import com.example.rest_security_api.dto.FileDto;
import com.example.rest_security_api.dto.UserCreateDto;
import com.example.rest_security_api.entity.Status;
import com.example.rest_security_api.integration.annotation.IT;
import com.example.rest_security_api.util.GetLocationFileUtil;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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


        MockMultipartFile file = new MockMultipartFile("file", "test", "text/plain", "test_content".getBytes());
        MockMultipartFile file1 = new MockMultipartFile("file", "test1", "text/plain", "test_content1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2", "text/plain", "test_content2".getBytes());


        mockMvc.perform(multipart("/api/v1/files")
                        .file(file)
                        .param("fileName", "test")
                        .param("bucketName", "timurul"))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/files")
                        .file(file1)
                        .param("fileName", "test1")
                        .param("bucketName", "timurul"))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/files")
                        .file(file2)
                        .param("fileName", "test2")
                        .param("bucketName", "timurul"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "Timur", password = "1234", authorities = "ADMIN")
    void getAll() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/files")
                        .param("bucketName", "timurul"))
                .andExpect(status().is2xxSuccessful()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<FileDto> list = objectMapper.readValue(contentAsString, new TypeReference<List<FileDto>>() {
        });
        assertThat(list).isNotEmpty();
        assertThat(list).hasSize(3);
    }


    @Test
    @Order(3)
    @WithMockUser(username = "Timur", password = "1234", authorities = "ADMIN")
    void getById() throws Exception {
        FileDto fileDto = FileDto.builder()
                .name("test")
                .location(GetLocationFileUtil.getLocation("timurul", "test"))
                .status(Status.ACTIVE)
                .createdBy("Timur")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/files/1"))
                .andExpect(status().is2xxSuccessful()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        FileDto fileDtoResult = objectMapper.readValue(contentAsString, new TypeReference<FileDto>() {
        });
        assertThat(fileDtoResult).isNotNull();
        System.out.println(fileDto);
        System.out.println(fileDtoResult);
    }


    @Test
    @Order(4)
    @WithMockUser(username = "Timur", password = "1234", authorities = "ADMIN")
    void deleteByName() throws Exception {
        mockMvc.perform(delete("/api/v1/files")
                        .param("fileName", "test1")
                        .param("bucketName", "timurul"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Order(5)
    @WithMockUser(username = "Timur", password = "1234", authorities = "ADMIN")
    void downloadFile() throws Exception {
        mockMvc.perform(get("/api/v1/files/download/test")
                        .param("bucketName", "timurul"))
                .andExpect(status().is2xxSuccessful()).andReturn();
    }

    @Test
    @Order(6)
    @WithMockUser(username = "Timur", password = "1234", authorities = "ADMIN")
    void updateFile() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "test", "text/plain", "new_content".getBytes());
        mockMvc.perform(multipart("/api/v1/files")
                        .file(file)
                        .param("fileName", "test")
                        .param("bucketName", "timurul"))
                .andExpect(status().is2xxSuccessful());
    }

    @AfterAll
    static void stop() {
        Minio.container.stop();
        Postgres.container.stop();
    }
}




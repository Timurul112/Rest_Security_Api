package com.example.rest_security_api.integration.rest;

import com.example.rest_security_api.container.initialization.Postgres;
import com.example.rest_security_api.dto.UserCreateDto;
import com.example.rest_security_api.dto.UserUpdateDto;
import com.example.rest_security_api.dto.UserUpdatePasswordDto;
import com.example.rest_security_api.integration.annotation.IT;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@ContextConfiguration(initializers = {Postgres.Initialization.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRestControllerV1IT {

    @Autowired
    private MockMvc mockMvc;


    @BeforeAll
    static void init() {
        Postgres.container.start();


        Flyway flyway = Flyway.configure()
                .dataSource(Postgres.container.getJdbcUrl(), Postgres.container.getUsername(), Postgres.container.getPassword())
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
    }

    @Test
    @Order(1)
    @WithMockUser(username = "Евгений", password = "1234", authorities = "ADMIN")
    void create() throws Exception {
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .username("Timur")
                .rawPassword("123")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequestBody = objectMapper.writeValueAsString(userCreateDto);


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "Евгений", password = "1234", authorities = "ADMIN")
    void findAll() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().is2xxSuccessful()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(response).contains("Timur");
    }

    @Test
    @Order(3)
    @WithMockUser(username = "Евгений", password = "1234", authorities = "ADMIN")
    void getById() throws Exception {
        String expectedUserDtoName = "Timur";
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().is2xxSuccessful()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertThat(response).contains(expectedUserDtoName);
    }

    @Test
    @Order(4)
    @WithMockUser(username = "Евгений", password = "1234", authorities = "ADMIN")
    void updateWithoutPassword() throws Exception {
        String expectedUserDtoId = "1";

        UserUpdateDto userUpdateDto = new UserUpdateDto("Евгений");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequestBody = objectMapper.writeValueAsString(userUpdateDto);
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().is2xxSuccessful()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).contains(expectedUserDtoId);
    }

    @Test
    @Order(5)
    @WithMockUser(username = "Евгений", password = "1234", authorities = "ADMIN")
    void updatePassword() throws Exception {
        UserUpdatePasswordDto updatePasswordDto = new UserUpdatePasswordDto("raw_password");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequestBody = objectMapper.writeValueAsString(updatePasswordDto);
        mockMvc.perform(put("/api/v1/users/reset-password/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "Евгений", password = "1234", authorities = "ADMIN")
    @Order(6)
    void deleteById() throws Exception {
        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().is2xxSuccessful());
    }


    @AfterAll
    static void stop() {
        Postgres.container.stop();
    }
}

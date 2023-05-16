package com.example.rest_security_api.integration.rest;

import com.example.rest_security_api.container.initialization.Localstack;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserRestControllerV1IT {

//    private final MockMvc mockMvc;


    @Test
    void findAll() throws Exception {
//        Localstack.container.getMappedPort()
//        mockMvc.perform(get("/api/v1/users"))
//                .andExpect();

    }

}
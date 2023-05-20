package com.example.rest_security_api.integration.rest;

import com.example.rest_security_api.container.initialization.Postgres;
import com.example.rest_security_api.dto.EventDto;
import com.example.rest_security_api.entity.*;
import com.example.rest_security_api.integration.annotation.IT;
import com.example.rest_security_api.repository.FileRepository;
import com.example.rest_security_api.repository.UserRepository;
import com.example.rest_security_api.service.EventService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(initializers = Postgres.Initialization.class)
public class EventRestControllerV1IT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventService eventService;


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
    @WithMockUser(username = "Timur", password = "1234", authorities = "ADMIN")
    void getAll() throws Exception {

        User user = User.builder()
                .id(1)
                .username("Timur")
                .password("123")
                .role(Role.ADMIN)
                .status(Status.ACTIVE)
                .build();

        userRepository.save(user);


        File file = File.builder()
                .id(1)
                .name("file_name")
                .createdBy(user.getUsername())
                .status(Status.ACTIVE)
                .location("location_file")
                .build();

        fileRepository.save(file);


        Event event = Event.builder()
                .id(1)
                .user(user)
                .file(file)
                .typeOperation(Operation.CREATION)
                .build();

        eventService.save(event);

        System.out.println();

        ObjectMapper objectMapper = new ObjectMapper();
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().is2xxSuccessful()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<EventDto> listDto = objectMapper.readValue(contentAsString, new TypeReference<List<EventDto>>() {
        });
        assertThat(listDto).isNotEmpty();
        assertThat(listDto).hasSize(1);
    }


    @Test
    @WithMockUser(username = "Timur", password = "1234", authorities = "ADMIN")
    void getEventById() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        EventDto eventDto = EventDto.builder()
                .userId(1)
                .fileId(1)
                .typeOperation(Operation.CREATION)
                .build();



        MvcResult mvcResult = mockMvc.perform(get("/api/v1/events/1"))
                .andExpect(status().is2xxSuccessful()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        EventDto eventDtoResult = objectMapper.readValue(contentAsString, new TypeReference<EventDto>() {
        });


        assertThat(eventDtoResult).isNotNull();
        assertThat(eventDtoResult).isEqualTo(eventDto);
    }


    @AfterAll
    static void stop() {
        Postgres.container.stop();
    }
}

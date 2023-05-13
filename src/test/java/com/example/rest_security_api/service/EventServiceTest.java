package com.example.rest_security_api.service;

import com.example.rest_security_api.dto.EventDto;
import com.example.rest_security_api.entity.*;
import com.example.rest_security_api.mapper.EventReadMapper;
import com.example.rest_security_api.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventReadMapper eventReadMapper;


    @InjectMocks
    private EventService eventService;


    @Test
    void save() {
        User user = User.builder()
                .id(1)
                .username("Timur")
                .status(Status.ACTIVE)
                .role(Role.USER)
                .password("password")
                .build();

        File file = File.builder()
                .id(1)
                .name("file_name")
                .location("location")
                .createdBy("user")
                .status(Status.ACTIVE)
                .build();
        Event event = Event.builder()
                .user(user)
                .file(file)
                .build();

        eventService.save(event);


        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void getAll() {
        User user = User.builder()
                .id(1)
                .username("Timur")
                .status(Status.ACTIVE)
                .role(Role.USER)
                .password("password")
                .build();

        File file = File.builder()
                .id(1)
                .name("file_name")
                .location("location")
                .createdBy("user")
                .status(Status.ACTIVE)
                .build();
        Event event = Event.builder()
                .user(user)
                .file(file)
                .build();

        EventDto eventDto = EventDto.builder()
                .fileId(file.getId())
                .userId(user.getId())
                .build();

        ArrayList<EventDto> eventDtos = new ArrayList<>();
        eventDtos.add(eventDto);


        ArrayList<Event> events = new ArrayList<>();
        events.add(event);

        doReturn(events).when(eventRepository).findAll();
        doReturn(eventDto).when(eventReadMapper).mapToDto(event);

        List<EventDto> actual = eventService.getAll();


        assertThat(actual).isNotEmpty();
        assertThat(actual).isEqualTo(eventDtos);
    }

    @Test
    void getById() {
        User user = User.builder()
                .id(1)
                .username("Timur")
                .status(Status.ACTIVE)
                .role(Role.USER)
                .password("password")
                .build();

        File file = File.builder()
                .id(1)
                .name("file_name")
                .location("location")
                .createdBy("user")
                .status(Status.ACTIVE)
                .build();
        Event event = Event.builder()
                .id(1)
                .user(user)
                .file(file)
                .build();

        Optional<Event> optionalEvent = Optional.of(event);

        doReturn(optionalEvent).when(eventRepository).findById(1);

        Optional<Event> actual = eventService.getById(1);

        assertThat(actual).isPresent();
        assertThat(actual).isEqualTo(optionalEvent);
    }
}
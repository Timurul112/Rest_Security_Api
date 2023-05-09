package com.example.rest_security_api.service;


import com.example.rest_security_api.dto.EventDto;
import com.example.rest_security_api.dto.FileReadDto;
import com.example.rest_security_api.dto.UserReadDto;
import com.example.rest_security_api.entity.Event;
import com.example.rest_security_api.entity.File;
import com.example.rest_security_api.entity.User;
import com.example.rest_security_api.mapper.FileReadMapper;
import com.example.rest_security_api.mapper.UserReadMapper;
import com.example.rest_security_api.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;


    @Transactional
    public void save(Event event) {
        eventRepository.save(event);
    }
}

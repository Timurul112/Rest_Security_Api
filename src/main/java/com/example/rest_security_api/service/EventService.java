package com.example.rest_security_api.service;


import com.example.rest_security_api.dto.EventDto;
import com.example.rest_security_api.entity.Event;
import com.example.rest_security_api.mapper.EventReadMapper;
import com.example.rest_security_api.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventReadMapper eventReadMapper;


    @Transactional
    public void save(Event event) {
        eventRepository.save(event);
    }

    public List<EventDto> getAll() {
        List<Event> eventsList = eventRepository.findAll();
        return eventsList.stream()
                .map(eventReadMapper::mapToDto).toList();
    }

    public Optional<Event> getById(Integer eventId) {
        return eventRepository.findById(eventId);
    }
}

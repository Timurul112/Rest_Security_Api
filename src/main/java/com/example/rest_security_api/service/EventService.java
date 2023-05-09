package com.example.rest_security_api.service;


import com.example.rest_security_api.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;


    public void save(EventDto eventDto) {




        eventRepository.save()

    }






}

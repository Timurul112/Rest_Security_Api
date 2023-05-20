package com.example.rest_security_api.service;


import com.example.rest_security_api.dto.EventDto;
import com.example.rest_security_api.entity.Event;
import com.example.rest_security_api.mapper.EventReadMapper;
import com.example.rest_security_api.repository.EventRepository;
import com.example.rest_security_api.util.AuthenticationUtil;
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

    public Optional<EventDto> getById(Integer eventId) {
        String authority = AuthenticationUtil.getAuthority();
        if (authority.equals("USER")) {
            String usernamePrincipal = AuthenticationUtil.getUsername();
            Event event = eventRepository.getById(eventId);
            String usernameDB = event.getUser().getUsername();
            if (!usernameDB.equals(usernamePrincipal)) {
                throw new RuntimeException("Access denied");
            } else{
                return Optional.of(eventReadMapper.mapToDto(event));
            }
        } else if ((authority.equals("ADMIN")) || authority.equals("MODERATOR")) {
            Event event = eventRepository.getById(eventId);
            return Optional.of(eventReadMapper.mapToDto(event));
        }
        return Optional.empty();
    }
}

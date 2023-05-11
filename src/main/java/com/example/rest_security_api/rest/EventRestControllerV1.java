package com.example.rest_security_api.rest;

import com.example.rest_security_api.dto.EventDto;
import com.example.rest_security_api.entity.Event;
import com.example.rest_security_api.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("api/v1/events")
@RestController
@RequiredArgsConstructor
public class EventRestControllerV1 {
    private final EventService eventService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public List<EventDto> getAll() {
        return eventService.getAll();
    }

    @GetMapping("/{id}") //оттестировать
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'USER')")
    @PostAuthorize("(hasAuthority('USER') and returnObject.get().user.username == authentication.principal.username)" +
            "or hasAnyAuthority('MODERATOR', 'ADMIN')")
    public Optional<Event> getEventById(@PathVariable(name = "id") Integer eventId) {
        return eventService.getById(eventId);
    }
}

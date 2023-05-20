package com.example.rest_security_api.rest;

import com.example.rest_security_api.dto.EventDto;
import com.example.rest_security_api.entity.Event;
import com.example.rest_security_api.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<EventDto>> getAll() {
        List<EventDto> eventListDto = eventService.getAll();
        return ResponseEntity.ok(eventListDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'USER')")
    public ResponseEntity<Optional<EventDto>> getEventById(@PathVariable(name = "id") Integer eventId) {
        Optional<EventDto> optionalEvent = eventService.getById(eventId);
        return ResponseEntity.ok(optionalEvent);
    }
}

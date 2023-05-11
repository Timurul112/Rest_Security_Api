package com.example.rest_security_api.mapper;

import com.example.rest_security_api.dto.EventReadDto;
import com.example.rest_security_api.entity.Event;

public class EventMapper implements Mapper<Event, EventReadDto> {

    @Override
    public EventReadDto mapToDto(Event entity) {
        return EventReadDto.builder()
                .fileId(entity.getFile().getId())
                .userId(entity.getUser().getId())
                .status(entity.getStatus())
                .build();
    }

    @Override
    public Event mapToEntity(EventReadDto dto) {
        return null;
    }
}

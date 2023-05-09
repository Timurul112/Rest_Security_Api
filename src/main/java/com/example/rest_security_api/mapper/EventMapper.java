package com.example.rest_security_api.mapper;

import com.example.rest_security_api.dto.EventDto;
import com.example.rest_security_api.entity.Event;

public class EventMapper implements Mapper<Event, EventDto> {

    @Override
    public EventDto mapToDto(Event entity) {
        return EventDto.builder()
                .fileId(entity.getFile().getId())
                .userId(entity.getUser().getId())
                .status(entity.getStatus())
                .build();
    }

    @Override
    public Event mapToEntity(EventDto dto) {
        return null;
    }
}

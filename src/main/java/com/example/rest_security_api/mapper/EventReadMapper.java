package com.example.rest_security_api.mapper;

import com.example.rest_security_api.dto.EventDto;
import com.example.rest_security_api.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventReadMapper implements Mapper<Event, EventDto> {


    @Override
    public EventDto mapToDto(Event entity) {

        return EventDto.builder()
                .userId(entity.getUser().getId())
                .fileId(entity.getFile().getId())
                .typeOperation(entity.getTypeOperation())
                .build();
    }

    @Override
    public Event mapToEntity(EventDto dto) {
        return null;
    }
}

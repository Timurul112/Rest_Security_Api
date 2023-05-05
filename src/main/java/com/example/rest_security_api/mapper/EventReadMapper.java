package com.example.rest_security_api.mapper;

import com.example.rest_security_api.dto.EventReadDto;
import com.example.rest_security_api.entity.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventReadMapper implements Mapper<Event, EventReadDto> {


    @Override
    public EventReadDto map(Event entity) {

        return EventReadDto.builder()
                .userId(entity.getUser().getId())
                .fileId(entity.getFile().getId())
                .status(entity.getStatus()).build();
    }
}

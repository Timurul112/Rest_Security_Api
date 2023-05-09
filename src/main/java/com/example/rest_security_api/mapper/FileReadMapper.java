package com.example.rest_security_api.mapper;

import com.example.rest_security_api.dto.FileReadDto;
import com.example.rest_security_api.entity.File;
import org.springframework.stereotype.Component;

@Component
public class FileReadMapper implements Mapper<File, FileReadDto> {
    @Override
    public FileReadDto map(File entity) {
        return FileReadDto.builder()
                .name(entity.getName())
                .status(entity.getStatus().name())
                .location(entity.getLocation())
                .createdBy(entity.getCreatedBy())
                .build();
    }
}

package com.example.rest_security_api.mapper;

import com.example.rest_security_api.dto.FileDto;
import com.example.rest_security_api.entity.File;
import org.springframework.stereotype.Component;

@Component
public class FileReadMapper implements Mapper<File, FileDto> {
    @Override
    public FileDto mapToDto(File entity) {
        return FileDto.builder()
                .name(entity.getName())
                .status(entity.getStatus())
                .location(entity.getLocation())
                .createdBy(entity.getCreatedBy())
                .build();
    }

    @Override
    public File mapToEntity(FileDto dto) {
        return File.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .status(dto.getStatus())
                .createdBy(dto.getCreatedBy())
                .build();
    }
}

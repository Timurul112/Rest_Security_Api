package com.example.rest_security_api.mapper;

import com.example.rest_security_api.dto.UserReadDto;
import com.example.rest_security_api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {
    @Override
    public UserReadDto mapToDto(User entity) {
        return UserReadDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .role(entity.getRole())
                .status(entity.getStatus())
                .fileKeys(entity.getFileKeys())
                .build();
    }

    @Override
    public User mapToEntity(UserReadDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .fileKeys(dto.getFileKeys())
                .status(dto.getStatus())
                .role(dto.getRole())
                .build();
    }
}

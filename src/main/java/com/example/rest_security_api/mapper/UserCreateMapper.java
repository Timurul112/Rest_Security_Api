package com.example.rest_security_api.mapper;

import com.example.rest_security_api.dto.UserCreateDto;
import com.example.rest_security_api.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<User, UserCreateDto> {
    private final PasswordEncoder passwordEncoder;

    @Override
    public User mapToEntity(UserCreateDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getRawPassword()))
                .build();
    }

    @Override
    public UserCreateDto mapToDto(User entity) {
        return null;
    }
}

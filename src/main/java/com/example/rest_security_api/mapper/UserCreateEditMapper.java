package com.example.rest_security_api.mapper;

import com.example.rest_security_api.dto.UserCreateEditDto;
import com.example.rest_security_api.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User> {
    private final PasswordEncoder passwordEncoder;


    public User copy(UserCreateEditDto userDto, User user) {
        user.setPassword(passwordEncoder.encode(userDto.getRawPassword()));
        user.setUsername(userDto.getUsername());
        return user;
    }




    @Override
    public User map(UserCreateEditDto entity) {
        return User.builder()
                .username(entity.getUsername())
                .password(passwordEncoder.encode(entity.getRawPassword()))
                .build();
    }
}

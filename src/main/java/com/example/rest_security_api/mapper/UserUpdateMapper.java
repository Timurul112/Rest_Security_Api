package com.example.rest_security_api.mapper;

import com.example.rest_security_api.dto.UserUpdateDto;
import com.example.rest_security_api.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserUpdateMapper {


    public User copy(UserUpdateDto userUpdateDto, User initialUser) {
        initialUser.setUsername(userUpdateDto.getUsername());
        return initialUser;
    }
}

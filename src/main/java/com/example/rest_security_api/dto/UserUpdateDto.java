package com.example.rest_security_api.dto;

import lombok.Getter;

@Getter
public class UserUpdateDto {
    String username;

    public UserUpdateDto(String username) {
        this.username = username;
    }
}

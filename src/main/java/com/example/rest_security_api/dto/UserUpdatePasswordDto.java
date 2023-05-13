package com.example.rest_security_api.dto;

import lombok.Getter;

@Getter
public class UserUpdatePasswordDto {
    String rawPassword;

    public UserUpdatePasswordDto(String rawPassword) {
        this.rawPassword = rawPassword;
    }
}

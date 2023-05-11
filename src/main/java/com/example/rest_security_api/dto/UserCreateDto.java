package com.example.rest_security_api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserCreateDto {
    String username;
    String rawPassword;
}

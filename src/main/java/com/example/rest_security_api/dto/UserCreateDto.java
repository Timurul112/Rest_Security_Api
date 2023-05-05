package com.example.rest_security_api.dto;

import com.example.rest_security_api.entity.Role;
import lombok.Builder;
import lombok.Value;
import org.springframework.validation.annotation.Validated;

@Value
@Builder
public class UserCreateDto {
    String username;
    String rawPassword;
}

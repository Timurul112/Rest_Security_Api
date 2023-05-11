package com.example.rest_security_api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserUpdateDto {
    String username;
}

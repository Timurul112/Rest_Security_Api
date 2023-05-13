package com.example.rest_security_api.dto;

import com.example.rest_security_api.entity.Role;
import com.example.rest_security_api.entity.Status;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UserReadDto {
    Integer id;
    String username;
    Role role;
    Status status;
}

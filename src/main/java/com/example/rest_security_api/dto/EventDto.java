package com.example.rest_security_api.dto;


import com.example.rest_security_api.entity.Operation;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EventDto {
    Integer userId;
    Integer fileId;
    Operation typeOperation;
}

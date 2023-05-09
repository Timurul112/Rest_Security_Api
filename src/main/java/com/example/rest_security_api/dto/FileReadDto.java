package com.example.rest_security_api.dto;


import com.example.rest_security_api.entity.Status;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FileReadDto {

    String name;
    String location;
    Status status;
    String createdBy;

}

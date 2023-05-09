package com.example.rest_security_api.dto;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FileReadDto {

    String name;
    String location;
    String status;
    String createdBy;

}

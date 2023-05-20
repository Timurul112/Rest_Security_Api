package com.example.rest_security_api.dto;


import com.example.rest_security_api.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FileDto {
    private String name;
    private String location;
    private Status status;
    private String createdBy;
}

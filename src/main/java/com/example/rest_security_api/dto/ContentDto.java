package com.example.rest_security_api.dto;

import lombok.Getter;

@Getter
public class ContentDto {

    String content;

    public ContentDto(String content) {
        this.content = content;
    }

    public ContentDto() {
    }
}

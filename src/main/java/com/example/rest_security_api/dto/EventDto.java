package com.example.rest_security_api.dto;


import com.example.rest_security_api.entity.Operation;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class EventDto {
    private Integer userId;
    private Integer fileId;
    private Operation typeOperation;

}

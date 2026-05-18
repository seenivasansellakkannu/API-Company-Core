package com.company.core.process.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SuccessResponse {

    private LocalDateTime timestamp;
    
    private int status;
    
    private String message;
    
}

package com.example.backend.dtos.Errors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String error;
    private String message;
    private long timestamp;


    public ErrorResponse(String message) {
        this.error = "invalid-request";
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}

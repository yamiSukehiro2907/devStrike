package com.example.backend.dtos.Response;

import lombok.Data;

import java.util.Date;

@Data
public class AuthenticationResponse {

    private String accessToken;
    private String refreshToken;

    public AuthenticationResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public AuthenticationResponse() {

    }
}

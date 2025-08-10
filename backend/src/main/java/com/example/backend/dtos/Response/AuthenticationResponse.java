package com.example.backend.dtos.Response;

import lombok.Data;

import java.util.Date;
@Data
public class AuthenticationResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Date refreshTokenExpiry;
    private Date accessTokenExpiry;
    private String username;

    public AuthenticationResponse(String accessToken, String refreshToken, Date refreshTokenExpiry, Date accessTokenExpiry, String username) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiry = accessTokenExpiry;
        this.refreshTokenExpiry = refreshTokenExpiry;
        this.username = username;
    }
}

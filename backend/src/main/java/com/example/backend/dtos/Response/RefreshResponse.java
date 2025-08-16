package com.example.backend.dtos.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshResponse {
    private String refreshToken;
    private String accessToken;
}

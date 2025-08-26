package com.example.backend.dtos.Authentication;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogOutRequest {
    private String refreshToken;
}

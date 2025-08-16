package com.example.backend.dtos.Authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogOutRequest {
    private String refreshToken;
}

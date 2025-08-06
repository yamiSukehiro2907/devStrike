package com.example.backend.dtos.Authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String uniqueIdentifier;
    private String password;
}

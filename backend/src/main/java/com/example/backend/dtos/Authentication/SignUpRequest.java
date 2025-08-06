package com.example.backend.dtos.Authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String email;
    private String username;
    private String name;
    private String password;
}

package com.example.backend.services.Authentication;

import com.example.backend.dtos.Authentication.RefreshRequest;
import com.example.backend.dtos.Authentication.SignUpRequest;
import com.example.backend.dtos.Response.AuthenticationResponse;
import com.example.backend.dtos.User.UserDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    UserDto createUser(SignUpRequest signUpRequest);

    AuthenticationResponse loginUser(String identifier, String hashedPassword);

    ResponseEntity<?> logOutUser(String authHeader);

    ResponseEntity<?> refreshUser(RefreshRequest refreshRequest);
}

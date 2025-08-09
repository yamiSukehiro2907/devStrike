package com.example.backend.services.Authentication;

import com.example.backend.dtos.Authentication.SignUpRequest;
import com.example.backend.dtos.User.UserDto;
import com.example.backend.entities.User;

public interface AuthService {

    UserDto createUser(SignUpRequest signUpRequest);

    User loginUser(String identifier, String hashedPassword);
}

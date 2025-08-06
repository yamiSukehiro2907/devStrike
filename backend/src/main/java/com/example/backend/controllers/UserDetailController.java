package com.example.backend.controllers;


import com.example.backend.dtos.Authentication.LoginRequest;
import com.example.backend.dtos.Authentication.SignUpRequest;
import com.example.backend.dtos.Errors.ErrorResponse;
import com.example.backend.entities.User;
import com.example.backend.repositories.User.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UserDetailController {

    public UserDetailController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/signup")
    public User signUpUser(@RequestBody SignUpRequest signUpRequest) {
        String name = signUpRequest.getName();
        String username = signUpRequest.getUsername();
        String password = signUpRequest.getPassword();
        String email = signUpRequest.getEmail();

        System.out.println("API Hit");
        if (name == null || username == null || password == null || email == null) {
            throw new RuntimeException("All fields are required");
        }

        List<User> alreadyExistingUser = userRepository.findByEmail(email);
        if (!alreadyExistingUser.isEmpty()) {
            throw new RuntimeException("Email already in use");
        }
        System.out.println("Checked duplicate email");

        alreadyExistingUser = userRepository.findByUsername(username);
        if (!alreadyExistingUser.isEmpty()) {
            throw new RuntimeException("Username already in use");
        }
        System.out.println("Checked duplicate username");

        String hashedPassword = bCryptPasswordEncoder.encode(password);
        System.out.println("hashed password");

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setEmail(email);
        user.setName(name);
        System.out.println("Reached here");
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        String identifier = loginRequest.getUniqueIdentifier();
        String hashedPassword = loginRequest.getPassword();
        if (identifier == null || hashedPassword == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid Credentials"));
        }
        return null;
    }
}

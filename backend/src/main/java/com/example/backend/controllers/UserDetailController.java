package com.example.backend.controllers;


import com.example.backend.dtos.Authentication.LoginRequest;
import com.example.backend.dtos.Authentication.RefreshRequest;
import com.example.backend.dtos.Authentication.SignUpRequest;
import com.example.backend.dtos.Response.AuthenticationResponse;
import com.example.backend.dtos.User.UserDto;
import com.example.backend.services.Authentication.AuthService;
import com.example.backend.services.UserDetail.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserDetailController {

    private final AuthService authService;

    public UserDetailController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody SignUpRequest signUpRequest) {
        UserDto createdUserDto = authService.createUser(signUpRequest);
        if (createdUserDto == null) {
            return new ResponseEntity<>("Failed to create user!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        String identifier = loginRequest.getUniqueIdentifier();
        String hashedPassword = loginRequest.getPassword();
        if (identifier == null || hashedPassword == null) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.BAD_REQUEST);
        }
        AuthenticationResponse authenticationResponse = authService.loginUser(identifier, hashedPassword);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username;
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            username = customUserDetails.getUsername();
            return authService.logOutUser(username);
        }
        return ResponseEntity.badRequest().body("Failed to logout the user!");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest refreshRequest) {
        if (refreshRequest == null || refreshRequest.getRefreshToken() == null) {
            return new ResponseEntity<>("All credentials required", HttpStatus.BAD_REQUEST);
        }
        return authService.refreshUser(refreshRequest);
    }
}

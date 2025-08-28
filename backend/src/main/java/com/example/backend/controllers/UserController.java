package com.example.backend.controllers;

import com.example.backend.dtos.User.UserDetailsUpdateRequest;
import com.example.backend.services.UserDetail.CustomUserDetails;
import com.example.backend.services.UserDetail.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

    }

    @PutMapping("/update-details")
    public ResponseEntity<?> updateUserDetails(@RequestBody UserDetailsUpdateRequest userDetailsUpdateRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            return userService.updateUser(customUserDetails.getUsername(), userDetailsUpdateRequest);
        }
        return new ResponseEntity<>("Authentication failed", HttpStatus.BAD_REQUEST);
    }
}

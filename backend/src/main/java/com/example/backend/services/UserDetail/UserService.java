package com.example.backend.services.UserDetail;


import com.example.backend.dtos.User.UserDetailsUpdateRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> updateUser(String username , UserDetailsUpdateRequest userDetailsUpdateRequest);
}

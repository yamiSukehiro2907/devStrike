package com.example.backend.services.UserDetail;

import com.example.backend.dtos.Response.UpdateUserDetailsResponse;
import com.example.backend.dtos.User.UserDetailsUpdateRequest;
import com.example.backend.entities.User;
import com.example.backend.repositories.User.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final UpdateUserDetailsResponse updateUserDetailsResponse = new UpdateUserDetailsResponse();

    @Override
    public ResponseEntity<?> updateUser(String username, UserDetailsUpdateRequest userDetailsUpdateRequest) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("User not found!", HttpStatus.BAD_REQUEST);
        }

        User user = userOptional.get();

        String newName = userDetailsUpdateRequest.getName();
        if (newName != null) {
            user.setName(newName);
        }

        String profilePathUrl = userDetailsUpdateRequest.getProfilePathUrl();

        if (profilePathUrl != null) {
            user.setProfilePathUrl(profilePathUrl);
        }

        user = userRepository.save(user);
        updateUserDetailsResponse.setProfilePathUrl(user.getProfilePathUrl());
        updateUserDetailsResponse.setName(user.getName());
        return new ResponseEntity<>(updateUserDetailsResponse, HttpStatus.OK);
    }
}

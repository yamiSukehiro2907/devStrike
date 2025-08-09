package com.example.backend.services.Authentication;

import com.example.backend.dtos.Authentication.SignUpRequest;
import com.example.backend.dtos.User.UserDto;
import com.example.backend.entities.User;
import com.example.backend.repositories.User.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private final UserDto createdUserDto = new UserDto();

    @Override
    @Transactional
    public UserDto createUser(SignUpRequest signUpRequest) {
        String name = signUpRequest.getName();
        String username = signUpRequest.getUsername();
        String password = signUpRequest.getPassword();
        String email = signUpRequest.getEmail();

        if (name == null || username == null || password == null || email == null) {
            throw new RuntimeException("All fields are required");
        }

        List<User> alreadyExistingUser = userRepository.findByEmail(email);
        if (!alreadyExistingUser.isEmpty()) {
            throw new RuntimeException("Email already in use");
        }

        alreadyExistingUser = userRepository.findByUsername(username);
        if (!alreadyExistingUser.isEmpty()) {
            throw new RuntimeException("Username already in use");
        }

        String hashedPassword = bCryptPasswordEncoder.encode(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setEmail(email);
        user.setName(name);

        User createdUser = userRepository.save(user);

        createdUserDto.setId(createdUser.getId());
        createdUserDto.setUsername(createdUser.getUsername());
        createdUserDto.setName(createdUser.getName());
        createdUserDto.setEmail(createdUser.getEmail());
        createdUserDto.setPassword(createdUser.getPassword());
        createdUserDto.setCreatedAt(createdUser.getCreatedAt());
        createdUserDto.setUpdatedAt(createdUser.getUpdatedAt());

        return createdUserDto;
    }

    @Override
    @Transactional
    public User loginUser(String identifier, String password) {
        List<User> findByUsername = userRepository.findByUsername(identifier);
        User user;
        if (findByUsername.isEmpty()) {
            List<User> findByEmail = userRepository.findByEmail(identifier);
            if (findByEmail.isEmpty()) {
                throw new RuntimeException("Invalid credentials");
            } else {
                user = findByEmail.getFirst();
            }
        } else {
            user = findByUsername.getFirst();
        }
        if (!bCryptPasswordEncoder.matches(password , user.getPassword())) {
            throw new RuntimeException("Incorrect Password");
        };
        return user;
    }
}

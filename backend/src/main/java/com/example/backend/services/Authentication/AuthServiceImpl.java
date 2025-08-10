package com.example.backend.services.Authentication;

import com.example.backend.dtos.Authentication.SignUpRequest;
import com.example.backend.dtos.Response.AuthenticationResponse;
import com.example.backend.dtos.User.UserDto;
import com.example.backend.entities.User;
import com.example.backend.repositories.User.UserRepository;
import com.example.backend.services.UserDetail.UserDetailServiceImp;
import com.example.backend.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final UserDetailServiceImp userDetailService;

    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, UserDetailServiceImp userDetailService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.userDetailService = userDetailService;
    }

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private final UserDto createdUserDto = new UserDto();

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

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

        Optional<User> alreadyExistingUser = userRepository.findByEmail(email);
        if (alreadyExistingUser.isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        alreadyExistingUser = userRepository.findByUsername(username);
        if (alreadyExistingUser.isPresent()) {
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
    public AuthenticationResponse loginUser(String identifier, String password) {
        Optional<User> user;
        if (isEmail(identifier)) user = userRepository.findByEmail(identifier);
        else user = userRepository.findByUsername(identifier);
        if (user.isEmpty()) throw new UsernameNotFoundException("User not found", null);
        String accessToken = jwtUtil.generateAccessToken(userDetailService.convert(user.get()));
        String refreshToken = jwtUtil.generateRefreshToken(userDetailService.convert(user.get()));
        Date refreshTokenExpiry = jwtUtil.getExpirationDate(refreshToken);
        Date accessTokenExpiry = jwtUtil.getExpirationDate(accessToken);
        user.get().setRefreshToken(refreshToken);
        userRepository.save(user.get());
        return new AuthenticationResponse(accessToken, refreshToken, refreshTokenExpiry, accessTokenExpiry, user.get().getUsername());

    }

    private boolean isEmail(String email) {
        if (email == null) return false;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

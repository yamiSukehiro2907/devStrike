package com.example.backend.services.Authentication;

import com.example.backend.dtos.Authentication.RefreshRequest;
import com.example.backend.dtos.Authentication.SignUpRequest;
import com.example.backend.dtos.Response.AuthenticationResponse;
import com.example.backend.dtos.Response.RefreshResponse;
import com.example.backend.dtos.User.UserDto;
import com.example.backend.entities.User;
import com.example.backend.repositories.User.UserRepository;
import com.example.backend.services.UserDetail.CustomUserDetails;
import com.example.backend.services.UserDetail.UserDetailServiceImp;
import com.example.backend.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        String role = signUpRequest.getRole() != null ? signUpRequest.getRole() : "user";

        if (name == null || username == null || password == null || email == null) {
            throw new RuntimeException("All fields are required");
        }

        Optional<User> alreadyExistingUser = userRepository.findByEmail(email);
        if (alreadyExistingUser.isPresent()) {
            return null;
        }

        alreadyExistingUser = userRepository.findByUsername(username);
        if (alreadyExistingUser.isPresent()) {
            return null;
        }

        String hashedPassword = bCryptPasswordEncoder.encode(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setEmail(email);
        user.setName(name);
        user.setRole(role);

        User createdUser = userRepository.save(user);

        createdUserDto.setId(createdUser.getId());
        createdUserDto.setUsername(createdUser.getUsername());
        createdUserDto.setName(createdUser.getName());
        createdUserDto.setEmail(createdUser.getEmail());
        createdUserDto.setPassword(createdUser.getPassword());
        createdUserDto.setCreatedAt(createdUser.getCreatedAt());
        createdUserDto.setUpdatedAt(createdUser.getUpdatedAt());
        createdUserDto.setRole(createdUser.getRole());

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

    @Override
    public ResponseEntity<?> logOutUser(String authHeader, String refreshToken) {
        try {
            String accessToken = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) accessToken = authHeader.substring(7);
            if (accessToken != null) {
                String username = jwtUtil.extractUsername(accessToken);
                if (jwtUtil.validateRefreshToken(refreshToken, userDetailService.loadUserByUsername(username))) {
                    Optional<User> user = userRepository.findByUsername(username);
                    if (user.isPresent()) {
                        user.get().setRefreshToken(null);
                        userRepository.save(user.get());
                        return new ResponseEntity<>("Logout Successful", HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Logout Failed", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("Logout Failed", HttpStatus.CONFLICT);
    }

    @Override
    public ResponseEntity<?> refreshUser(RefreshRequest refreshRequest) {
        RefreshResponse refreshResponse = new RefreshResponse();
        try {
            String refreshToken = refreshRequest.getRefreshToken();
            String username = jwtUtil.extractUsername(refreshToken);
            CustomUserDetails customUserDetails = userDetailService.loadUserByUsername(username);
            if (jwtUtil.validateRefreshToken(refreshToken, customUserDetails)) {
                Optional<User> user = userRepository.findByUsername(username);
                if (user.isPresent()) {
                    if (user.get().getRefreshToken() == null) {
                        return ResponseEntity.badRequest().body("Please login again!");
                    }
                    String newRefreshToken = jwtUtil.generateRefreshToken(customUserDetails);
                    String newAccessToken = jwtUtil.generateAccessToken(customUserDetails);
                    refreshResponse.setRefreshToken(newRefreshToken);
                    user.get().setRefreshToken(newRefreshToken);
                    userRepository.save(user.get());
                    refreshResponse.setRefreshToken(newRefreshToken);
                    refreshResponse.setAccessToken(newAccessToken);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Token refresh failed!");
        }
        return new ResponseEntity<>(refreshResponse, HttpStatus.OK);
    }
}

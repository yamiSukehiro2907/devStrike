package com.example.backend.services.Authentication;

import com.example.backend.dtos.Authentication.SignUpRequest;
import com.example.backend.dtos.Response.AuthenticationResponse;
import com.example.backend.dtos.User.UserDto;
import com.example.backend.entities.RefreshToken;
import com.example.backend.entities.User;
import com.example.backend.repositories.RefreshToken.RefreshTokenRepository;
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

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final UserDetailServiceImp userDetailService;

    private final RefreshTokenRepository refreshTokenRepository;

    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, UserDetailServiceImp userDetailService, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.userDetailService = userDetailService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private final UserDto createdUserDto = new UserDto();

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    private final AuthenticationResponse authenticationResponse = new AuthenticationResponse();


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
        String refreshTokenString = jwtUtil.generateRefreshToken(userDetailService.convert(user.get()));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenString);
        refreshToken.setUserEmail(user.get().getEmail());

        refreshToken.setExpiration(jwtUtil.getExpirationDate(refreshTokenString));
        refreshTokenRepository.save(refreshToken);

        authenticationResponse.setAccessToken(accessToken);
        authenticationResponse.setRefreshToken(refreshToken.getToken());


        return authenticationResponse;

    }

    private boolean isEmail(String email) {
        if (email == null) return false;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public ResponseEntity<?> logOutUser(String refreshTokenString) {
        try {
            Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(refreshTokenString);
            if (refreshToken.isEmpty()) {
                return ResponseEntity.badRequest().body("LogOut failed.. Please login again!");
            }
            refreshTokenRepository.deleteById(refreshTokenString);
            return new ResponseEntity<>("LogOut Successful!", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("LogOut failed!");
        }
    }

    @Override
    public ResponseEntity<?> refreshUser(String oldRefreshTokenString) {

        try {
            Optional<RefreshToken> oldRefreshToken = refreshTokenRepository.findById(oldRefreshTokenString);
            if (oldRefreshToken.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid Refresh Token!");
            }
            if (oldRefreshToken.get().getExpiration().getTime() < System.currentTimeMillis()) {
                refreshTokenRepository.delete(oldRefreshToken.get());
                throw new RuntimeException("Refresh Token has expired. Please log in again!");
            }

            Optional<User> user = userRepository.findByEmail(oldRefreshToken.get().getUserEmail());

            if (user.isEmpty()) {
                throw new RuntimeException("Invalid refresh token!");
            }

            CustomUserDetails customUserDetails = userDetailService.convert(user.get());
            String accessToken = jwtUtil.generateAccessToken(customUserDetails);
            String refreshTokenString = jwtUtil.generateRefreshToken(customUserDetails);

            refreshTokenRepository.delete(oldRefreshToken.get());

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(refreshTokenString);
            refreshToken.setExpiration(jwtUtil.getExpirationDate(refreshTokenString));
            refreshToken.setUserEmail(oldRefreshToken.get().getUserEmail());

            refreshTokenRepository.save(refreshToken);

            authenticationResponse.setRefreshToken(refreshToken.getToken());
            authenticationResponse.setAccessToken(accessToken);


            return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);

        } catch (Exception e) {
            throw new RuntimeException("Token refresh failed!");
        }

    }
}

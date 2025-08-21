package com.example.backend.services.Platform;

import com.example.backend.dtos.Platform.AddPlatformRequest;
import com.example.backend.dtos.Platform.AdminPlatformResponse;
import com.example.backend.dtos.Platform.PlatformDto;
import com.example.backend.dtos.Platform.UserPlatformDto;
import com.example.backend.entities.Platform;
import com.example.backend.entities.User;
import com.example.backend.entities.UserPlatform;
import com.example.backend.repositories.Platform.PlatformRepository;
import com.example.backend.repositories.Platform.UserPlatformRepository;
import com.example.backend.repositories.User.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PlatformServiceImpl implements PlatformService {

    public UserPlatformRepository userPlatformRepository;

    public PlatformRepository platformRepository;

    public UserRepository userRepository;

    public PlatformServiceImpl(UserPlatformRepository userPlatformRepository, PlatformRepository platformRepository, UserRepository userRepository) {
        this.userPlatformRepository = userPlatformRepository;
        this.platformRepository = platformRepository;
        this.userRepository = userRepository;
    }

    private final AdminPlatformResponse adminPlatformResponse = new AdminPlatformResponse();

    private final UserPlatform userPlatform = new UserPlatform();

    @Override
    public ResponseEntity<?> findByUserId(Long userId) {

        PlatformDto platformDto = new PlatformDto();
        platformDto.setUserId(userId);

        List<UserPlatformDto> userPlatforms = userPlatformRepository.findByUserId(userId);
        for (UserPlatformDto temp : userPlatforms) {
            if (Objects.equals(temp.getPlatform_name(), "Github")) {
                platformDto.setGithubUsername(temp.getPlatformUsername());
            } else if (Objects.equals(temp.getPlatform_name(), "Leetcode")) {
                platformDto.setLeetcodeUsername(temp.getPlatformUsername());
            } else if (Objects.equals(temp.getPlatform_name(), "GeeksForGeeks")) {
                platformDto.setGeeksForGeeksUsername(temp.getPlatformUsername());
            }
        }
        return new ResponseEntity<>(platformDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addPlatforms(Long userId, String platformName) {
        Platform platform = new Platform();
        platform.setPlatformName(platformName);
        Platform createdPlatform = platformRepository.save(platform);
        adminPlatformResponse.setAdminId(userId);
        adminPlatformResponse.setPlatform_name(createdPlatform.getPlatformName());
        adminPlatformResponse.setPlatform_id(createdPlatform.getId());
        return new ResponseEntity<>(adminPlatformResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> add(Long userId, AddPlatformRequest addPlatformRequest) {
        Platform platform;
        Optional<Platform> platform1 = platformRepository.findPlatformId(addPlatformRequest.getPlatform_name());
        if (platform1.isEmpty()) {
            return ResponseEntity.badRequest().body("Platform not found!!");
        }
        Optional<User> user = userRepository.findByUserId(userId);
        platform = platform1.get();
        userPlatform.setPlatform(platform);
        userPlatform.setUsername(addPlatformRequest.getUsername());
        userPlatform.setUser(user.);
        return null;
    }
}
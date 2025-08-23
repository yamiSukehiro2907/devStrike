package com.example.backend.services.Platform;

import com.example.backend.dtos.Platform.*;
import com.example.backend.entities.Platform;
import com.example.backend.entities.User;
import com.example.backend.entities.UserPlatform;
import com.example.backend.repositories.Platform.PlatformRepository;
import com.example.backend.repositories.Platform.UserPlatformRepository;
import com.example.backend.repositories.User.UserRepository;
import jakarta.transaction.Transactional;
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

    private final AddPlatformResponse addPlatformResponse = new AddPlatformResponse();


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
    @Transactional
    public ResponseEntity<?> add(String username, AddPlatformRequest addPlatformRequest) {
        Platform platform;
        Optional<Platform> platform1 = platformRepository.findPlatformId(addPlatformRequest.getPlatform_name());
        if (platform1.isEmpty()) {
            return ResponseEntity.badRequest().body("Platform not found!!");
        }
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        Optional<UserPlatformPair> userPlatformPair = userPlatformRepository.findByPlatformIdAndUserId(platform1.get().getId(), user.get().getId());
        if (userPlatformPair.isPresent()) {
            return ResponseEntity.badRequest().body("User already registered for this platform!");
        }
        UserPlatform userPlatform = new UserPlatform();
        userPlatform.setPlatform(platform1.get());
        userPlatform.setUser(user.get());
        userPlatform.setUsername(addPlatformRequest.getUsername());
        UserPlatform createdUserPlatform = userPlatformRepository.save(userPlatform);
        addPlatformResponse.setUsername(createdUserPlatform.getUser().getUsername());
        addPlatformResponse.setPlatformId(createdUserPlatform.getPlatform().getId());
        addPlatformResponse.setUserPlatformId(createdUserPlatform.getId());
        addPlatformResponse.setPlatformUsername(createdUserPlatform.getUsername());
        return new ResponseEntity<>(addPlatformResponse, HttpStatus.CREATED);
    }
}
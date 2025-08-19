package com.example.backend.services.Platform;

import com.example.backend.dtos.Platform.AdminPlatformResponse;
import com.example.backend.dtos.Platform.PlatformDto;
import com.example.backend.dtos.Platform.UserPlatformDto;
import com.example.backend.entities.Platform;
import com.example.backend.repositories.Platform.PlatformRepository;
import com.example.backend.repositories.Platform.UserPlatformRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PlatformServiceImpl implements PlatformService {

    public UserPlatformRepository userPlatformRepository;

    public PlatformRepository platformRepository;

    public PlatformServiceImpl(UserPlatformRepository userPlatformRepository, PlatformRepository platformRepository) {
        this.userPlatformRepository = userPlatformRepository;
        this.platformRepository = platformRepository;
    }

    private final AdminPlatformResponse adminPlatformResponse = new AdminPlatformResponse();

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
}
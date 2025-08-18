package com.example.backend.services.Platform;

import com.example.backend.dtos.Platform.PlatformDto;
import com.example.backend.dtos.Platform.UserPlatformDto;
import com.example.backend.repositories.PlatformRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PlatformServiceImpl implements PlatformService {

    public PlatformRepository platformRepository;

    public PlatformServiceImpl(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    @Override
    public ResponseEntity<?> findByUserId(Long userId) {

        PlatformDto platformDto = new PlatformDto();
        platformDto.setUserId(userId);

        List<UserPlatformDto> userPlatforms = platformRepository.findByUserId(userId);
        for (UserPlatformDto temp : userPlatforms) {
            if (Objects.equals(temp.getPlatform_name(), "Github")) {
                platformDto.setGithubPath(temp.getPath());
            } else if (Objects.equals(temp.getPlatform_name(), "Leetcode")) {
                platformDto.setLeetcodePath(temp.getPath());
            } else if (Objects.equals(temp.getPlatform_name(), "GeeksForGeeks")) {
                platformDto.setGeeksForGeeksPath(temp.getPath());
            }
        }
        return new ResponseEntity<>(platformDto, HttpStatus.OK);
    }
}
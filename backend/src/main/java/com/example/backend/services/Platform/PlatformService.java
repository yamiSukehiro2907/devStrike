package com.example.backend.services.Platform;

import org.springframework.http.ResponseEntity;

public interface PlatformService {

    ResponseEntity<?> findByUserId(Long userId);

    ResponseEntity<?> addPlatforms(Long userId , String platformName);
}

package com.example.backend.services.Platform;

import com.example.backend.dtos.Platform.AddPlatformRequest;
import com.example.backend.dtos.Platform.UpdatePlatformRequest;
import org.springframework.http.ResponseEntity;

public interface PlatformService {

    ResponseEntity<?> findByUserId(Long userId);

    ResponseEntity<?> addPlatforms(Long userId, String platformName);

    ResponseEntity<?> add(String username, AddPlatformRequest addPlatformRequest);

    ResponseEntity<?> updatePlatform(Long userId, UpdatePlatformRequest updatePlatformRequest);
}

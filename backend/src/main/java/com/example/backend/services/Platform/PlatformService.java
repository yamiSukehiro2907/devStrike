package com.example.backend.services.Platform;

import com.example.backend.dtos.Platform.AddPlatformRequest;
import org.springframework.http.ResponseEntity;

public interface PlatformService {

    ResponseEntity<?> findByUserId(Long userId);

    ResponseEntity<?> addPlatforms(Long userId , String platformName);

    ResponseEntity<?> add(Long userId , AddPlatformRequest addPlatformRequest);
}

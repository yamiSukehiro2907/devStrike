package com.example.backend.controllers;

import com.example.backend.services.Platform.PlatformService;
import com.example.backend.services.UserDetail.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/platforms")
public class PlatformDetailsController {

    private final PlatformService platformService;

    public PlatformDetailsController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping("/my-platforms")
    public ResponseEntity<?> getPlatforms() {
        System.out.println("my-platforms API HIT");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId;
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            userId = customUserDetails.getUserId();
            return platformService.findByUserId(userId);
        } else {
            return ResponseEntity.badRequest().body("Authentication Error!");
        }
    }
}

package com.example.backend.controllers;

import com.example.backend.dtos.Platform.PlatformDto;
import com.example.backend.services.Platform.PlatformService;
import com.example.backend.services.PlatformService.PlatformDetailsService;
import com.example.backend.services.UserDetail.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class ServiceController {

    public final PlatformService platformService;

    public final PlatformDetailsService platformDetailsService;

    public ServiceController(PlatformService platformService, PlatformDetailsService platformDetailsService) {
        this.platformService = platformService;
        this.platformDetailsService = platformDetailsService;
    }

    @GetMapping("/github")
    public void madeCommitToday() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId;
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            userId = customUserDetails.getUserId();
            if (customUserDetails.getPlatformDetails().isEmpty()) {
                fillPlatformDetails(userId, customUserDetails);
            }
            boolean madeCommit = platformDetailsService.hasUserMadeCommitToday(customUserDetails.getPlatformDetails().get("github"));
            if (!madeCommit) {
                System.out.println("Have not made any commit....");
            }
        }
    }

    private void fillPlatformDetails(Long userId, CustomUserDetails customUserDetails) {
        ResponseEntity<?> response = platformService.findByUserId(userId);
        if (response.hasBody()) {
            Map<String, String> map = new HashMap<>();
            PlatformDto platforms = (PlatformDto) response.getBody();
            if (platforms == null) return;
            if (platforms.getGithubUsername() != null) {
                map.put("github", platforms.getGithubUsername());
            }
            if (platforms.getLeetcodeUsername() != null) {
                map.put("leetcode", platforms.getLeetcodeUsername());
            }
            if (platforms.getGeeksForGeeksUsername() != null) {
                map.put("geeksforgeeks", platforms.getGeeksForGeeksUsername());
            }
            customUserDetails.setPlatformDetails(map);
        }
    }
}

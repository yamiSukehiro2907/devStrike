package com.example.backend.controllers;

import com.example.backend.dtos.Platform.AdminPlatformRequest;
import com.example.backend.services.Platform.PlatformService;
import com.example.backend.services.UserDetail.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/platforms")
public class PlatformDetailsController {

    private final PlatformService platformService;

    public PlatformDetailsController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping("/my-platforms")
    public ResponseEntity<?> getPlatforms() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        Long userId;
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            userId = customUserDetails.getUserId();
            System.out.println("Get all platforms for this user : " + userId);
            return platformService.findByUserId(userId);
        } else {
            return ResponseEntity.badRequest().body("Authentication Error!");
        }
    }

    @PostMapping("/add-platform")
    public ResponseEntity<?> addPlatforms(@RequestBody AdminPlatformRequest adminPlatformRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId;
        String role;
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            userId = customUserDetails.getUserId();
            role = customUserDetails.getRole();
            if (!role.equals("admin")) {
                return new ResponseEntity<>("Only admins are allowed!", HttpStatus.UNAUTHORIZED);
            }
            return platformService.addPlatforms(userId, adminPlatformRequest.getPlatform_name());
        }
        return ResponseEntity.badRequest().body("Platform Addition failed");
    }
}

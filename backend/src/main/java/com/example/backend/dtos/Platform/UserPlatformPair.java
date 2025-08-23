package com.example.backend.dtos.Platform;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPlatformPair {
    private Long userId;
    private Long platformId;

    public UserPlatformPair(Long userId , Long platformId) {
        this.userId = userId;
        this.platformId = platformId;
    }
}

package com.example.backend.dtos.Platform;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPlatformDto {

    private String platform_name;

    private String username;

    public UserPlatformDto(String platform_name, String username) {
        this.platform_name = platform_name;
        this.username = username;
    }
}

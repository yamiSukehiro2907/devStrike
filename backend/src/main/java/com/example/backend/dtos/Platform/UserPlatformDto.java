package com.example.backend.dtos.Platform;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPlatformDto {

    private String platform_name;

    private String path;

    public UserPlatformDto(String platform_name, String path) {
        this.platform_name = platform_name;
        this.path = path;
    }
}

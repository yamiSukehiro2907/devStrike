package com.example.backend.dtos.Platform;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPlatformRequest {
    private String platform_name;

    private String username;
}

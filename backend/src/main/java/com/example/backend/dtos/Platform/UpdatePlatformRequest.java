package com.example.backend.dtos.Platform;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdatePlatformRequest {
    private String platformName;
    private String newUsername;
}

package com.example.backend.dtos.Platform;

import lombok.Data;

@Data
public class AddPlatformResponse {
    private String username;
    private Long userPlatformId;
    private Long platformId;
    private String platformUsername;
}

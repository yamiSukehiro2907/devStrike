package com.example.backend.dtos.Platform;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminPlatformResponse {
    private Long adminId;
    private Long platform_id;
    private String platform_name;
}

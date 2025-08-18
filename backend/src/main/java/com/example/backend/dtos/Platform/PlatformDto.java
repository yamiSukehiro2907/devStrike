package com.example.backend.dtos.Platform;

import lombok.Data;

@Data
public class PlatformDto {

    private Long userId;

    private String githubUsername;

    private String leetcodeUsername;

    private String geeksForGeeksUsername;
}

package com.example.backend.dtos.Platform;

import lombok.Data;

@Data
public class PlatformDto {

    private Long userId;

    private String githubPath;

    private String leetcodePath;

    private String geeksForGeeksPath;
}

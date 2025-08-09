package com.example.backend.dtos.User;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserDto {

    private Long id;

    private String username;

    private String name;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String password;
}

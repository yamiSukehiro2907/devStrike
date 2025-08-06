package com.example.backend.dtos.User;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {

    private Long id;

    private String username;

    private String name;

    private String email;

    private LocalDate createdAt;

    private LocalDate updatedAt;


    private Boolean isActive;

    private String password;
}

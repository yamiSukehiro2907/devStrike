package com.example.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "platform_user")
public class UserPlatform {
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "platform_id", nullable = false)
    private Long platformId;

    @Column(name = "path", nullable = false)
    private String path;
}

package com.example.backend.repositories;

import com.example.backend.entities.Platform;
import com.example.backend.entities.UserPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlatformRepository extends JpaRepository<Platform, Long> {

    @Query("Select * from platform_user where user_id")
    List<UserPlatform> findByUserId(Long id);
}

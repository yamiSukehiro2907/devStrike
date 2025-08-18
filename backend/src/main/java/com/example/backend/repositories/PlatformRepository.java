package com.example.backend.repositories;

import com.example.backend.dtos.Platform.UserPlatformDto;
import com.example.backend.entities.UserPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PlatformRepository extends JpaRepository<UserPlatform, Long> {

    @Query("SELECT NEW com.example.backend.dtos.Platform.UserPlatformDto(up.platform.platformName, up.path) FROM UserPlatform up WHERE up.user.id = :userId")
    List<UserPlatformDto> findByUserId(@Param("userId") Long userId);
}
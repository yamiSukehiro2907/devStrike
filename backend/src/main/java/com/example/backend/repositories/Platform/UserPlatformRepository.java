package com.example.backend.repositories.Platform;

import com.example.backend.dtos.Platform.UserPlatformDto;
import com.example.backend.dtos.Platform.UserPlatformPair;
import com.example.backend.entities.UserPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPlatformRepository extends JpaRepository<UserPlatform, Long> {

    @Query("SELECT NEW com.example.backend.dtos.Platform.UserPlatformDto(up.platform.platformName, up.username) FROM UserPlatform up WHERE up.user.id = :userId")
    List<UserPlatformDto> findByUserId(@Param("userId") Long userId);


    @Query("SELECT NEW com.example.backend.dtos.Platform.UserPlatformPair(up.user.id, up.platform.id) FROM UserPlatform up WHERE up.user.id = :userId AND up.platform.id = :platformId")
    Optional<UserPlatformPair> findByPlatformIdAndUserId(@Param("platformId") Long platformId, @Param("userId") Long userId);

    @Query("SELECT up FROM UserPlatform up WHERE up.user.id = :user_id AND up.platform.platformName = :platform_name")
    Optional<UserPlatform> findByPlatformNameAndUserId(@Param("platform_name") String platformName, @Param("user_id") Long userId);

}
package com.example.backend.repositories.Platform;

import com.example.backend.entities.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlatformRepository extends JpaRepository<Platform , Long> {

    @Query("SELECT NEW com.example.backend.entities.Platform(p.id, p.platformName) FROM Platform p WHERE p.platformName = :platform_name")
    Optional<Platform> findPlatformId(String platform_name);
}
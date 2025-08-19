package com.example.backend.repositories.Platform;

import com.example.backend.entities.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends JpaRepository<Platform , Long> {

}

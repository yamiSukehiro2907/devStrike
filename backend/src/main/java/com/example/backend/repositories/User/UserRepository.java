package com.example.backend.repositories.User;

import com.example.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("Select u from User u where u.username = :username")
    List<User> findByUsername(String username);

    @Query("Select u from User u where u.email = :email")
    List<User> findByEmail(String email);
}

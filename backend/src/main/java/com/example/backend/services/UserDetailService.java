package com.example.backend.services;


import com.example.backend.repositories.User.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserDetailService {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;
}

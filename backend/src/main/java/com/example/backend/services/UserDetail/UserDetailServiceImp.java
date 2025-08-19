package com.example.backend.services.UserDetail;

import com.example.backend.entities.User;
import com.example.backend.repositories.User.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailServiceImp implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) throw new UsernameNotFoundException("User not found", null);
        return new CustomUserDetails(optionalUser.get().getId(), optionalUser.get().getUsername(), optionalUser.get().getPassword(), optionalUser.get().getEmail(), optionalUser.get().getRole(), new ArrayList<>());
    }


    public CustomUserDetails loadUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) throw new UsernameNotFoundException("Email not found", null);
        return new CustomUserDetails(optionalUser.get().getId(), optionalUser.get().getUsername(), optionalUser.get().getPassword(), optionalUser.get().getEmail(), optionalUser.get().getRole(), new ArrayList<>());
    }

    public CustomUserDetails convert(User user) {
        return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRole(), new ArrayList<>());
    }
}

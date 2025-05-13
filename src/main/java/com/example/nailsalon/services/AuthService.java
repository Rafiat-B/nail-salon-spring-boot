package com.example.nailsalon.services;

import com.example.nailsalon.enums.UserRoles;
import com.example.nailsalon.models.Users;
import com.example.nailsalon.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(String username, String password, String email) {
        Users newUsers = Users.builder().username(username).password(passwordEncoder.encode(password)).email(email).role(UserRoles.ROLE_USER).build();

        userRepository.save(newUsers);
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}

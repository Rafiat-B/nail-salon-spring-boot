package com.example.nailsalon.services;

import com.example.nailsalon.models.Users;
import com.example.nailsalon.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public Users getCurrentUser(UserDetails userDetails) {
        if(userDetails == null){
            return null;
        }
        return userRepository.findByUsername(userDetails.getUsername()).orElse(null);
    }
}

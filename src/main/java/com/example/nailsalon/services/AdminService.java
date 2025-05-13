package com.example.nailsalon.services;

import com.example.nailsalon.enums.UserRoles;
import com.example.nailsalon.models.Admin;
import com.example.nailsalon.repository.AdminRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.Optional;

@Service
@Transactional
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostConstruct
    public void initAdmin() {
        if (adminRepository.findByUsername("admin").isEmpty()) {
            Admin admin = Admin.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("123"))
                    .role(UserRoles.ROLE_ADMIN)
                    .email("admin@example.com")
                    .build();
            adminRepository.save(admin);
        }
    }

    public Optional<Admin> findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Admin getCurrentAdmin(UserDetails userDetails) {
        if(userDetails == null){
            return null;
        }
        return adminRepository.findByUsername(userDetails.getUsername()).orElse(null);
    }
}

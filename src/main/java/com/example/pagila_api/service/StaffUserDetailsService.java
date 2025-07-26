package com.example.pagila_api.service;

import com.example.pagila_api.model.Staff;
import com.example.pagila_api.repository.StaffRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class StaffUserDetailsService implements UserDetailsService {

    private final StaffRepository staffRepository;

    public StaffUserDetailsService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Staff staff = staffRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Staff not found with username: " + username));

        // Map roles (assuming roles are stored in JWT; adjust if stored in Staff entity)
        return new User(
                staff.getUsername(),
                staff.getPassword() != null ? staff.getPassword() : "",
                staff.getActive(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                Collections.emptyList() // Roles are extracted from JWT via JwtAuthenticationConverter
        );
    }
}
package com.example.pagila_api.controller;

import com.example.pagila_api.model.dto.JwtResponse;
import com.example.pagila_api.model.dto.LoginRequest;
import com.example.pagila_api.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/token")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest loginRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String jwt = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}

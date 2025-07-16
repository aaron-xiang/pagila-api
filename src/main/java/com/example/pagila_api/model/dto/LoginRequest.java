package com.example.pagila_api.model.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}

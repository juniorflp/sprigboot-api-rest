package com.example.curso_api_rest_java.dto.UserAuth;

import com.example.curso_api_rest_java.model.user.UserRole;

public record RegisterDTO(String login, String password, UserRole role, String firstName, String lastName, Boolean isActive) {
    public RegisterDTO(String login, String password) {
        this(login, password, null, null, null, true);
    }
}

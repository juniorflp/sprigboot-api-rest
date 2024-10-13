package com.example.curso_api_rest_java.dto;

import com.example.curso_api_rest_java.model.user.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}

package com.example.curso_api_rest_java.dto.UserAuth;

import com.example.curso_api_rest_java.model.user.User;
import com.example.curso_api_rest_java.model.user.UserRole;

public record UserResponseDTO(
        String login,
        UserRole role,
        String firstName,
        String lastName,
        Boolean isActive
) {
    public UserResponseDTO(User user) {
        this(
                user.getLogin(),
                user.getRole(),
                user.getFirstName(),
                user.getLastName(),
                user.getActive()
        );
    }
}
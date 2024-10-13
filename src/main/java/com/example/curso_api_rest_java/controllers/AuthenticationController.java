package com.example.curso_api_rest_java.controllers;


import com.example.curso_api_rest_java.dto.UserAuth.AuthenticationDTO;
import com.example.curso_api_rest_java.dto.UserAuth.LoginResponseDTO;
import com.example.curso_api_rest_java.dto.UserAuth.RegisterDTO;
import com.example.curso_api_rest_java.dto.UserAuth.UserResponseDTO;
import com.example.curso_api_rest_java.exceptions.UnauthorizedException;
import com.example.curso_api_rest_java.exceptions.UserAlreadyExistsException;
import com.example.curso_api_rest_java.infra.security.TokenService;
import com.example.curso_api_rest_java.model.user.User;
import com.example.curso_api_rest_java.model.user.UserRole;
import com.example.curso_api_rest_java.repositories.UserRepository;
import com.example.curso_api_rest_java.services.AuthorizationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/v1")
@Tag(name = "Auth", description = "Endpoints for Get Auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return new LoginResponseDTO(token);
    }

    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody @Valid RegisterDTO data) {
        if (this.repository.findByLogin(data.login()) != null) {
            throw new UserAlreadyExistsException("The user already exists");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        UserRole role = (data.role() != null) ? data.role() : UserRole.ROLE_USER;
        Boolean isActive = (data.isActive() != null) ? data.isActive() : true;

        User newUser = new User(data.login(),
                encryptedPassword,
                role,
                data.firstName(),
                data.lastName(),
                isActive
        );
        this.repository.save(newUser);

        UserResponseDTO userResponseDTO = new UserResponseDTO(newUser);

        return userResponseDTO;
    }


    @GetMapping("/me")
    public UserResponseDTO getMe() {
        User user = authorizationService.getAuthenticatedUser();

        if (user == null) {
            throw new UnauthorizedException("Unauthenticated user");
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO(user);

        return userResponseDTO;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDTO> findAll() {
        List<User> users = repository.findAll();
        List<UserResponseDTO> userDTOs = users.stream()
                .map(user -> new UserResponseDTO(user))
                .collect(Collectors.toList());

        return userDTOs;

    }

}

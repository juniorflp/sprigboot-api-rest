package com.example.curso_api_rest_java.controllers;


import com.example.curso_api_rest_java.dto.UserAuth.AuthenticationDTO;
import com.example.curso_api_rest_java.dto.UserAuth.LoginResponseDTO;
import com.example.curso_api_rest_java.dto.UserAuth.RegisterDTO;
import com.example.curso_api_rest_java.dto.UserAuth.UserResponseDTO;
import com.example.curso_api_rest_java.infra.security.TokenService;
import com.example.curso_api_rest_java.model.user.User;
import com.example.curso_api_rest_java.model.user.UserRole;
import com.example.curso_api_rest_java.repositories.UserRepository;
import com.example.curso_api_rest_java.services.AuthorizationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

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
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {

        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", e.getMessage()));
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        try {
            if (this.repository.findByLogin(data.login()) != null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "the user already exists"));
            }

            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

            UserRole role = (data.role() != null) ? data.role() : UserRole.ROLE_USER;

            Boolean isActive = (data.isActive() != null)? data.isActive(): true;

            User newUser = new User(data.login(),
                    encryptedPassword,
                    role,
                    data.firstName(),
                    data.lastName(),
                    isActive
            );
            this.repository.save(newUser);

            UserResponseDTO userResponseDTO = new UserResponseDTO(newUser);

            return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping(value ="/me", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMe(){

        User user = authorizationService.getAuthenticatedUser();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Usuário não autenticado"));
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO(user);

        return ResponseEntity.ok(userResponseDTO);

    }
}

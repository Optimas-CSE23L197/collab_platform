package com.example.collaboration.collab.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collaboration.collab.dto.ApiResponse;
import com.example.collaboration.collab.dto.RegisterRequestDTO;
import com.example.collaboration.collab.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class RegisterController {
    private final AuthService authService;

    public RegisterController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        boolean isUserExists = authService.isUserExists(registerRequest.getUserEmail());

        if (!isUserExists) {
            authService.registerUser(registerRequest);
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "User registered successfully", null,
                    null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "User already exists", null,
                    null);
            return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
        }
    }
}

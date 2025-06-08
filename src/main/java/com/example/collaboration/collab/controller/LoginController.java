package com.example.collaboration.collab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collaboration.collab.dto.LoginRequestDTO;
import com.example.collaboration.collab.dto.ApiResponse;
import com.example.collaboration.collab.service.AuthService;
import com.example.collaboration.collab.utils.JwtToken;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class LoginController {

    // autowired basicallty injects the AuthService bean into this controller
    // so that we can use its methods to handle authentication logic
    @Autowired
    private final AuthService authService;

    @Autowired
    private JwtToken jwtToken;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    // handle login requests
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequestDTO loginRequet) {
        // first check user exist or not
        if (!authService.isUserExists(loginRequet.getUserEmail())) {
            ApiResponse loginResponse = new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "User does not exist", null,
                    null);
            return new ResponseEntity<>(loginResponse, HttpStatus.UNAUTHORIZED);
        }
        boolean isAuthenticated = authService.authenticateUser(loginRequet);
        if (isAuthenticated) {
            String token = jwtToken.tokenGenerator(loginRequet.getUserEmail());
            ApiResponse loginResponse = new ApiResponse(HttpStatus.OK.value(), "Login Successful", token, null);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } else {
            ApiResponse loginResponse = new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid Credentials", null,
                    null);
            return new ResponseEntity<>(loginResponse, HttpStatus.UNAUTHORIZED);
        }
    }
}

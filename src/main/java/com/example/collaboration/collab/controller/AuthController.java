package com.example.collaboration.collab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.example.collaboration.collab.dto.ApiResponseDTO;
import com.example.collaboration.collab.dto.UserRegisterDTO;
import com.example.collaboration.collab.service.UserService;
import com.example.collaboration.collab.service.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // register a new user
    @PostMapping("/register/user")
    public ResponseEntity<ApiResponseDTO> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        System.out.println("Registering user: " + userRegisterDTO.getUserPassword());
        userService.registerUser(userRegisterDTO);
        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), null, "User Register Successfull", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // login a user
    @PostMapping("/login/user")
    public ResponseEntity<ApiResponseDTO> loginUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRegisterDTO.getUserEmail(),
                            userRegisterDTO.getUserPassword()));

            if (authentication.isAuthenticated()) {
                String jwtToken = jwtService.generateToken(userRegisterDTO.getUserEmail());
                ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), jwtToken, "Login Successfull",
                        null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
        }

        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.UNAUTHORIZED.value(), null, "Wrong Credintials",
                null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("Hello from AuthController", HttpStatus.OK);
    }
}

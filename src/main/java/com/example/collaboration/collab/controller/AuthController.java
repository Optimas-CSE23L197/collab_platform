package com.example.collaboration.collab.controller;

import java.time.Duration;
import java.util.List;
import java.util.Map;

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
import org.springframework.security.core.GrantedAuthority;

import com.example.collaboration.collab.dto.ApiResponseDTO;
import com.example.collaboration.collab.dto.LoginRequestDTO;
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
    public ResponseEntity<ApiResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getUserName(),
                            loginRequestDTO.getUserPassword()));

            if (authentication.isAuthenticated()) {
                String mainRole = authentication.getAuthorities().stream()
                        .findFirst()
                        .map(GrantedAuthority::getAuthority)
                        .orElse("ROLE_USER");

                long expiryTimeOfToken = mainRole.equals(List.of("ROLE_HOD, ROLE_CLERK,ROLE_EMPLOYEE").toString())
                        ? Duration.ofMinutes(15).toMillis()
                        : Duration.ofHours(1).toMillis();

                Map<String, Object> claims = Map.of("role", mainRole);
                String token = jwtService.generateToken(loginRequestDTO.getUserName(), claims, expiryTimeOfToken);

                ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), token, "Login Successfull", null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
        }

        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.UNAUTHORIZED.value(), null, "Wrong Credintials",
                null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // login a employee
    @PostMapping("/login/employee")
    public ResponseEntity<ApiResponseDTO> loginEmployee(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getUserName(),
                            loginRequestDTO.getUserPassword()));

            if (authentication.isAuthenticated()) {
                String mainRole = authentication.getAuthorities().stream()
                        .findFirst()
                        .map(GrantedAuthority::getAuthority)
                        .orElse("ROLE_USER");

                long expiryTimeOfToken = mainRole.equals(List.of("ROLE_HOD, ROLE_CLERK,ROLE_EMPLOYEE").toString())
                        ? Duration.ofMinutes(15).toMillis()
                        : Duration.ofHours(1).toMillis();

                Map<String, Object> claims = Map.of("role", mainRole);
                String token = jwtService.generateToken(loginRequestDTO.getUserName(), claims, expiryTimeOfToken);

                ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), token, "Login Successfull", null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
        }

        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.UNAUTHORIZED.value(), null, "Wrong Credintials",
                null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}

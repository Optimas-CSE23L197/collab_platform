package com.example.collaboration.collab.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RegisterRequestDTO {
    @NotBlank(message = "User name is required")
    private String userName;

    @NotBlank(message = "User email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String userEmail;

    @NotBlank(message = "User password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String userPassword;

    @NotBlank(message = "User phone is required")
    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    private String userPhone;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String userAddress;
}

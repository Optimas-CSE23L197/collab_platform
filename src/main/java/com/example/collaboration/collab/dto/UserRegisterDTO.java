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
public class UserRegisterDTO {
    private String userId;

    @NotBlank(message = "User ID cannot be blank")
    private String userName;

    @NotBlank(message = "User email cannot be blank")
    @Email(message = "Invalid email format")
    @Size(max = 50, message = "Email cannot exceed 50 characters")
    private String userEmail;

    @NotBlank(message = "User password cannot be blank")
    @Size(min = 6, max = 20, message = "Password must be between 8 and 20 characters")
    private String userPassword;

    @NotBlank(message = "User phone cannot be blank")
    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    @Size(min = 10, message = "Phone number must be at least 10 characters")
    private String userPhone;

    private String userAddress;

    @NotBlank(message = "User role cannot be blank")
    private String userRole;
}

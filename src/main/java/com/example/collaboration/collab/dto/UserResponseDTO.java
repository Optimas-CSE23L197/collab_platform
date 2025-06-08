package com.example.collaboration.collab.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserResponseDTO {
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userAddress;

    public UserResponseDTO(String userName, String userEmail, String userPhone, String userAddress) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userAddress = userAddress;

    }
}

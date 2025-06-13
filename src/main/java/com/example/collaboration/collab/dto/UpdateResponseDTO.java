package com.example.collaboration.collab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UpdateResponseDTO {
    private String userName;
    private String userPassword;
    private String userPhone;
    private String userAddress;
}

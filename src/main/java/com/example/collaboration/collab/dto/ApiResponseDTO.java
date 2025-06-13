package com.example.collaboration.collab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ApiResponseDTO {
    private int response_status;
    private String response_token;
    private String response_message;
    private Object response_data;
}
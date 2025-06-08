package com.example.collaboration.collab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ApiResponse {
    private int response_Status;
    private String response_Message;
    private String response_token;
    private Object response_data;
}

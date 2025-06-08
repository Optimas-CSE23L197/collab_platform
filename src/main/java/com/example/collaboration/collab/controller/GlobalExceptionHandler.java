package com.example.collaboration.collab.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.collaboration.collab.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception e) {
        ApiResponse loginResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An error occurred: " + e.getMessage(), null, null);
        return new ResponseEntity<>(loginResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

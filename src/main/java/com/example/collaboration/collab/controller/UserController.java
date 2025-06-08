package com.example.collaboration.collab.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collaboration.collab.dto.ApiResponse;
import com.example.collaboration.collab.dto.UserResponseDTO;
import com.example.collaboration.collab.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // get user profile by email
    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable String userId) {
        var user = userService.getUserDetails(userId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "User fetched successfully", null, user);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    // update user profile
    @PutMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse> updateUserProfile(@PathVariable String userId,
            @Valid @RequestBody UserResponseDTO userResponseDTO) {
        var user = userService.updateUser(userId, userResponseDTO);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "User updated successfully", null, user);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    // delete user profile
    @DeleteMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse> deleteUserProfile(@PathVariable String userId) {
        userService.deleteUser(userId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "User deleted successfully", null, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}

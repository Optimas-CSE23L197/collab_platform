package com.example.collaboration.collab.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collaboration.collab.dto.ApiResponse;
import com.example.collaboration.collab.dto.ComplaintResponseDTO;
import com.example.collaboration.collab.dto.UserResponseDTO;
import com.example.collaboration.collab.service.ComplaintService;
import com.example.collaboration.collab.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ComplaintService complaintService;

    public UserController(UserService userService, ComplaintService complaintService) {
        this.userService = userService;
        this.complaintService = complaintService;
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

    // user complaint register
    @PostMapping("/{userId}/complaint")
    public ResponseEntity<ApiResponse> registerComplaint(
            @Valid @RequestBody ComplaintResponseDTO complaintResponseDTO) {
        complaintService.registerComplaint(complaintResponseDTO);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Complaint registered succfully", null, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    // get complaint by user id
    @GetMapping("/{userId}/complaint")
    public ResponseEntity<ApiResponse> getAllComplaintByUserId(@PathVariable String userId) {
        var complaint = complaintService.getAllComplaintsByUserId(userId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "All Complaint", null, complaint);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    // get complaint by complaint id
    @GetMapping("/{userId}/complaint/{complaintId}")
    public ResponseEntity<ApiResponse> getComplaintById(@PathVariable String complaintId) {
        var complaint = complaintService.getComplaintById(complaintId);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Complaint fetched successfully", null,
                complaint);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    // edit complaint
    @PutMapping("/{userId}/complaint/{complaintId}")
    public ResponseEntity<ApiResponse> editComplaint(@PathVariable String userId, @PathVariable String complaintId,
            @RequestBody @Valid ComplaintResponseDTO complaintResponseDTO) {
        complaintService.updateComplaint(complaintId, complaintResponseDTO);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Complaint update successfully", null,
                null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}

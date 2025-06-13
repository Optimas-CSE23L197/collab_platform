package com.example.collaboration.collab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collaboration.collab.dto.ApiResponseDTO;
import com.example.collaboration.collab.dto.ComplaintRequestDTO;
import com.example.collaboration.collab.dto.ComplaintUpdateRequest;
import com.example.collaboration.collab.dto.UpdateResponseDTO;
import com.example.collaboration.collab.service.UserService;
import com.example.collaboration.collab.service.ComplaintService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ComplaintService complaintService;
 
    // api for getting user profile
    @GetMapping("/profile/me")
    public ResponseEntity<ApiResponseDTO> getUserProfile() {
        var user = userService.getUserProfile();
        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), null, "User Fetch Successfully", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // api for edit user profile
    @PatchMapping("/profile/update")
    public ResponseEntity<ApiResponseDTO> updateUserProfile(@Valid @RequestBody UpdateResponseDTO updateResponseDTO) {
        var user = userService.updateUserProfile(updateResponseDTO);
        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), null, "User Update Successfully", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // api for register complaint
    @PostMapping("/complaint/register")
    public ResponseEntity<ApiResponseDTO> registerComplaint(
            @Valid @RequestBody ComplaintRequestDTO complaintRequestDTO) {
        var complaint = complaintService.registerComplaint(complaintRequestDTO);
        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), null, "Complaint Registered Successful",
                complaint);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // api for update complaint
    @PatchMapping("/complaint/update")
    public ResponseEntity<ApiResponseDTO> updateComplaint(
            @Valid @RequestBody ComplaintUpdateRequest complaintUpdateRequest) {
        var complaint = complaintService.updateComplaint(complaintUpdateRequest);
        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), null, "Complaint update successfully",
                complaint);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // fetch all complaint by user
    @GetMapping("/complaint")
    public ResponseEntity<ApiResponseDTO> getMyAllComplaint() {
        var complaints = complaintService.getAllComplaints();
        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), null, "Complaints fetched successfully",
                complaints);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // api for get complaint by id
    @GetMapping("/complaint/{complaintId}")
    public ResponseEntity<ApiResponseDTO> getMyComplaint(@PathVariable String complaintId) {
        var complaint = complaintService.getComplaintById(complaintId);
        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), null, "Complaint fetched successfully",
                complaint);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

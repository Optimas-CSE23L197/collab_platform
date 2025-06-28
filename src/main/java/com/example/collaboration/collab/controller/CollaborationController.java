package com.example.collaboration.collab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collaboration.collab.dto.ApiResponseDTO;
import com.example.collaboration.collab.dto.RoomJoinRequestDTO;
import com.example.collaboration.collab.dto.RoomRequestDTO;
import com.example.collaboration.collab.service.CollaborationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/collaboration")
public class CollaborationController {

    @Autowired
    private CollaborationService collaborationService;

    // create a collaboration room where multiple department can join
    @PreAuthorize("hasAnyRole('HOD','CLERK')")
    @PostMapping("/create-room")
    public ResponseEntity<ApiResponseDTO> createRoom(@Valid @RequestBody RoomRequestDTO roomRequestDTO) {
        collaborationService.createRoom(roomRequestDTO);
        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), null, "Room created successfully", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // department which member are joining room
    @PreAuthorize("hasAnyRole('HOD','CLERK','EMPLOYEE')")
    @PostMapping("/join-room-by-department")
    public ResponseEntity<ApiResponseDTO> joinRoomByDepartment() {
        collaborationService.joinDepartment(null);
        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), null, "Room Join successful",
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // employee join room
    @PreAuthorize("hasAnyRole('HOD','CLERK','EMPLOYEE')")
    @PostMapping("/join-room")
    public ResponseEntity<ApiResponseDTO> joinRoom(@Valid @RequestBody RoomJoinRequestDTO roomJoinRequestDTO) {
        collaborationService.joinRoom(roomJoinRequestDTO);
        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), null, "Room Join Successfully", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

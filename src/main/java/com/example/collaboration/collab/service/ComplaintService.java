package com.example.collaboration.collab.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.collaboration.collab.dto.ComplaintRequestDTO;
import com.example.collaboration.collab.dto.ComplaintUpdateRequest;
import com.example.collaboration.collab.model.Complaint;
import com.example.collaboration.collab.model.User;
import com.example.collaboration.collab.repository.ComplaintRepository;

@Service
public class ComplaintService {

    @Autowired
    private UserService userService;

    @Autowired
    private ComplaintRepository complaintRepository;

    // generate complaint id
    public String generateComplaintId(ComplaintRequestDTO complaintRequestDTO) {
        String namePart = complaintRequestDTO.getDepartmentId();
        Random random = new Random();
        int randomNumber = random.nextInt(100000, 999999);
        return namePart + randomNumber;
    }

    // register new complaint
    public Complaint registerComplaint(ComplaintRequestDTO complaintRequestDTO) {
        String userEmail = userService.validateUserAuthentication();

        User user = userService.getUserByEmail(userEmail);

        Complaint complaint = new Complaint();
        complaint.setComplaintId(generateComplaintId(complaintRequestDTO));
        complaint.setComplaintTitle(complaintRequestDTO.getComplaintTitle());
        complaint.setComplaintDescription(complaintRequestDTO.getComplaintDescription());

        complaint.setComplaintStatus(Complaint.ComplaintStatus.PENDING);
        complaint.setComplaintPriority(
                Complaint.ComplaintPriority.valueOf(complaintRequestDTO.getComplaintPriority().toUpperCase()));

        complaint.setComplaintCreatedAt(LocalDateTime.now());
        complaint.setComplaintUpdatedAt(LocalDateTime.now());
        complaint.setDepartmentId(complaintRequestDTO.getDepartmentId());
        complaint.setUser(user);

        // Step 5: Save the complaint
        return complaintRepository.save(complaint);
    }

    // update complaint
    public Complaint updateComplaint(ComplaintUpdateRequest complaintUpdateRequest) {

        String userEmail = userService.validateUserAuthentication();

        if (userService.validateUserAuthentication() == null) {
            throw new RuntimeException("User not authenticated");
        }

        Optional<Complaint> complaintOptional = complaintRepository
                .findByComplaintId(complaintUpdateRequest.getComplaintId());

        if (complaintOptional.isEmpty()) {
            throw new RuntimeException("Complaint not found with ID: " + complaintUpdateRequest.getComplaintId());
        }

        Complaint complaint = complaintOptional.get();

        if (!complaint.getUser().getUserEmail().equals(userEmail)) {
            throw new RuntimeException("You are not authorized to update this complaint.");
        }

        if (complaint.getComplaintStatus() == Complaint.ComplaintStatus.RESOLVED) {
            throw new RuntimeException("Complaint already resolved, cannot update.");
        }

        if (complaintUpdateRequest.getComplaintTitle() != null)
            complaint.setComplaintTitle(complaintUpdateRequest.getComplaintTitle());
        if (complaintUpdateRequest.getComplaintDescription() != null)
            complaint.setComplaintDescription(complaintUpdateRequest.getComplaintDescription());
        if (complaintUpdateRequest.getDepartmentId() != null)
            complaint.setDepartmentId(complaintUpdateRequest.getDepartmentId());
        complaintRepository.save(complaint);
        return complaint;
    }

    // fetch all complaints by user
    public Complaint getAllComplaints() {
        String userEmail = userService.validateUserAuthentication();

        User user = userService.getUserByEmail(userEmail);

        if (user == null) {
            throw new RuntimeException("User not found with email: " + userEmail);
        }

        Optional<Complaint> complaints = complaintRepository.findByUser_userId(user.getUserId());

        if (complaints.isEmpty()) {
            throw new RuntimeException("No complaints found for user: " + userEmail);
        }

        Complaint complaint = complaints.get();

        if (!complaint.getUser().getUserEmail().equals(userEmail)) {
            throw new RuntimeException("You are not authorized to view these complaints.");
        }

        return complaint;
    }

    // get complaint by id
    public Complaint getComplaintById(String complaintId) {

        String userEmail = userService.validateUserAuthentication();

        Optional<Complaint> comOptional = complaintRepository.findByComplaintId(complaintId);

        if (comOptional.isEmpty()) {
            throw new RuntimeException("Complaint not found with id: " + complaintId);
        }

        Complaint complaint = comOptional.get();

        if (!complaint.getUser().getUserEmail().equals(userEmail)) {
            throw new RuntimeException("You are not authorized to view this complaint.");
        }

        return complaint;
    }

}

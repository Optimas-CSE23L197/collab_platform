package com.example.collaboration.collab.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.collaboration.collab.dto.ComplaintResponseDTO;
import com.example.collaboration.collab.model.Complaint;
import com.example.collaboration.collab.model.User;
import com.example.collaboration.collab.repository.ComplaintRepository;
import com.example.collaboration.collab.repository.UserRepository;

@Service
public class ComplaintService {
    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    // generate complaint id
    public String generateComplaintId(ComplaintResponseDTO complaintResponseDTO) {
        String complaintName = complaintResponseDTO.getDeptId();
        Random random = new Random();
        int complaintNumber = random.nextInt(10000, 99999);
        return complaintName + complaintNumber;
    }

    // register new complaint
    public void registerComplaint(ComplaintResponseDTO complaintResponseDTO) {
        Optional<Complaint> complaintOptional = complaintRepository
                .findByComplaintId(complaintResponseDTO.getComplaintId());

        User user = userRepository.findByUserId(complaintResponseDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (complaintOptional.isPresent()) {
            throw new IllegalArgumentException("Complaint already exist");
        }

        LocalDate complaintRegisterAt = LocalDate.parse(complaintResponseDTO.getComplaintDateAt());
        LocalDate complaintResolveAt = LocalDate.parse(complaintResponseDTO.getComplaintResolveAt());

        if (complaintRegisterAt.isAfter(complaintResolveAt)) {
            throw new RuntimeException("Complaint date cannot be before resolve date");
        }

        Complaint complaint = new Complaint();
        try {
            complaint.setComplaintId(generateComplaintId(complaintResponseDTO));
        } catch (RuntimeException e) {
            throw new RuntimeException("Error generating complaint ID: " + e.getMessage());
        }
        complaint.setUser(user);
        complaint.setDeptId(complaintResponseDTO.getDeptId());
        complaint.setComplaintName(complaintResponseDTO.getComplaintName());
        complaint.setComplaintDescription(complaintResponseDTO.getComplaintDescription());
        complaint.setComplaintStatus("Pending");
        complaint.setComplaintDateAt(complaintResponseDTO.getComplaintDateAt());
        complaint.setComplaintResolveAt(complaintResponseDTO.getComplaintResolveAt());
        complaint.setEmployeeName(complaintResponseDTO.getEmployeeName());
        complaintRepository.save(complaint);
    }

    // update complaint by id
    public ComplaintResponseDTO updateComplaint(String complaintId, ComplaintResponseDTO complaintResponseDTO) {

        Optional<Complaint> complaintOptional = complaintRepository.findByComplaintId(complaintId);
        if (complaintOptional.isEmpty()) {
            throw new RuntimeException("Complaint not found with id: " + complaintId);
        }

        Complaint complaint = complaintOptional.get();

        if (complaint.getComplaintStatus().equals("Resolved")) {
            throw new RuntimeException("Complaint already resolved, cannot update");
        }

        complaint.setComplaintName(complaintResponseDTO.getComplaintName());
        complaint.setComplaintDescription(complaintResponseDTO.getComplaintDescription());
        complaint.setComplaintDateAt(complaintResponseDTO.getComplaintDateAt());
        complaintRepository.save(complaint);
        return new ComplaintResponseDTO(
                complaint.getComplaintId(),
                complaint.getUser().getUserName(),
                complaint.getUser().getUserId(),
                complaint.getDeptId(),
                complaint.getComplaintName(),
                complaint.getComplaintDescription(),
                complaint.getComplaintStatus(),
                complaint.getComplaintDateAt(),
                complaint.getComplaintResolveAt(),
                complaint.getEmployeeName());
    }

    // get all complaints by user id
    public Object getAllComplaintsByUserId(String userId) {
        Optional<List<Complaint>> complaintOptional = Optional.of(complaintRepository.findByUser_userId(userId));

        if (complaintOptional.isEmpty()) {
            throw new RuntimeException("No complaints found for user with id: " + userId);
        }

        return complaintOptional.get();
    }

    // get complaint by complaint id
    public Object getComplaintById(String complaintId) {
        Optional<Complaint> complaintOptional = complaintRepository.findByComplaintId(complaintId);

        if (complaintOptional.isEmpty()) {
            throw new RuntimeException("No complaints found for user with id: " + complaintId);
        }

        Complaint complaint = complaintOptional.get();
        return new ComplaintResponseDTO(
                complaint.getComplaintId(),
                complaint.getUser().getUserName(),
                complaint.getUser().getUserId(),
                complaint.getDeptId(),
                complaint.getComplaintName(),
                complaint.getComplaintDescription(),
                complaint.getComplaintStatus(),
                complaint.getComplaintDateAt(),
                complaint.getComplaintResolveAt(),
                complaint.getEmployeeName());
    }
}

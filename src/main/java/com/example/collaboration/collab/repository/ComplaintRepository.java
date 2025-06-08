package com.example.collaboration.collab.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.collaboration.collab.model.Complaint;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, String> {
    Optional<Complaint> findByComplaintId(String complaintId);

    List<Complaint> findByUser_userId(String userId);
}

package com.example.collaboration.collab.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.collaboration.collab.model.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, String> {
    Optional<Request> findByRequestId(String requestId);
}

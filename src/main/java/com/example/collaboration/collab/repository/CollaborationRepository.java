package com.example.collaboration.collab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.collaboration.collab.model.Collaboration;

@Repository
public interface CollaborationRepository extends JpaRepository<Collaboration, String> {
    Collaboration findByRoomId(String roomId);
}

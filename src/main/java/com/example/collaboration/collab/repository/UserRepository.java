package com.example.collaboration.collab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.collaboration.collab.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUserId(String userId);

    User findByUserEmail(String userEmail);
}

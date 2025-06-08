package com.example.collaboration.collab.repository;

import java.util.Optional;

import com.example.collaboration.collab.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserEmail(String userEmail);

    Optional<User> findByUserId(String userId);
}

package com.example.collaboration.collab.service;

import java.util.Optional;

import com.example.collaboration.collab.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.collaboration.collab.dto.UserResponseDTO;
import com.example.collaboration.collab.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // fetch user details by email
    public UserResponseDTO getUserDetails(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        User user = userOptional.get();
        return new UserResponseDTO(
                user.getUserName(),
                user.getUserEmail(),
                user.getUserPhone(),
                user.getUserAddress());
    }

    // update user profile
    public UserResponseDTO updateUser(String userId, UserResponseDTO userResponseDTO) {
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        User user = userOptional.get();
        user.setUserName(userResponseDTO.getUserName());
        user.setUserEmail(userResponseDTO.getUserEmail());
        user.setUserPhone(userResponseDTO.getUserPhone());
        user.setUserAddress(userResponseDTO.getUserAddress());
        userRepository.save(user);
        return new UserResponseDTO(
                user.getUserName(),
                user.getUserEmail(),
                user.getUserPhone(),
                user.getUserAddress());
    }

    // delete user profile
    public void deleteUser(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        User user = userOptional.get();
        userRepository.delete(user);
    }

}

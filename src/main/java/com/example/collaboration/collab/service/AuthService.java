package com.example.collaboration.collab.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.collaboration.collab.dto.LoginRequestDTO;
import com.example.collaboration.collab.dto.RegisterRequestDTO;
import com.example.collaboration.collab.model.User;
import com.example.collaboration.collab.repository.UserRepository;

@Service
public class AuthService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    // check if the user is authenticated
    public boolean authenticateUser(LoginRequestDTO loginRequest) {
        Optional<User> userOptional = userRepository.findByUserEmail(loginRequest.getUserEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordEncoder.matches(loginRequest.getUserPassword(), user.getUserPassword());
        }
        return false;
    }

    // check user existence by email
    public boolean isUserExists(String userEmail) {
        return userRepository.findByUserEmail(userEmail).isPresent();
    }

    // register a new user
    public void registerUser(RegisterRequestDTO registerRequest) {
        Optional<User> userOptional = userRepository.findByUserEmail(registerRequest.getUserEmail());

        if (userOptional.isPresent()) {
            throw new IllegalArgumentException("User already exists with email: " + registerRequest.getUserEmail());
        }

        User user = new User();
        try {
            user.setUserId(createUserId(registerRequest));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        user.setUserName(registerRequest.getUserName());
        user.setUserEmail(registerRequest.getUserEmail());
        user.setUserPhone(registerRequest.getUserPhone());
        user.setUserPassword(passwordEncoder.encode(registerRequest.getUserPassword()));
        user.setUserAddress(registerRequest.getUserAddress());
        userRepository.save(user);
    }

    // create different userid for each user
    public String createUserId(RegisterRequestDTO registerRequestDTO) throws ClassNotFoundException {
        String[] nameParts = registerRequestDTO.getUserName().split("\\s+");
        String firstName = nameParts[0].toLowerCase();
        Random random = new Random();
        int randomNumber = random.nextInt(1000, 9999);
        String userId = firstName + randomNumber;
        System.out.println("Generated User ID: " + userId);
        return userId;
    }
}

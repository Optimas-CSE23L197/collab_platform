package com.example.collaboration.collab.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.collaboration.collab.config.CustomUserDetails;
import com.example.collaboration.collab.dto.UpdateResponseDTO;
import com.example.collaboration.collab.dto.UserRegisterDTO;
import com.example.collaboration.collab.dto.UserResponseDTO;
import com.example.collaboration.collab.model.User;
import com.example.collaboration.collab.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // for spring security
    // load user details for authentication
    // leter we user jwt token for authentication then we can remove this method
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userEmail = username;
        User user = userRepository.findByUserEmail(userEmail);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + userEmail);
        }
        return new CustomUserDetails(user);
    }

    // generate user id
    public String generateUserId(UserRegisterDTO userRegisterDTO) {
        String[] nameParts = userRegisterDTO.getUserName().split("\s+");
        String userName = nameParts[0].toLowerCase();

        Random random = new Random();

        int randomNumber = random.nextInt(100000, 999999);
        return userName + randomNumber;
    }

    // check user authentication
    public User getUserByEmail(String userEmail) {
        userEmail = validateUserAuthentication();

        User user = userRepository.findByUserEmail(userEmail);

        if (user == null) {
            throw new RuntimeException("User not found with email: " + userEmail);
        }

        return user;
    }

    // validate user authentication
    public String validateUserAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    // register a new user
    public void registerUser(UserRegisterDTO userRegisterDTO) {

        System.out.println("User Password: " + userRegisterDTO.getUserPassword());

        User user = userRepository.findByUserEmail(userRegisterDTO.getUserEmail());

        String rawPassword = userRegisterDTO.getUserPassword();
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // check user already exist with email id
        if (user != null) {
            throw new RuntimeException("User already exists");
        }

        user = new User();
        user.setUserId(generateUserId(userRegisterDTO));
        user.setUserName(userRegisterDTO.getUserName());
        user.setUserEmail(userRegisterDTO.getUserEmail());
        user.setUserPhone(userRegisterDTO.getUserPhone());
        user.setUserPassword(passwordEncoder.encode(userRegisterDTO.getUserPassword()));
        System.out.println("User Password: " + userRegisterDTO.getUserPassword());
        user.setUserRole(userRegisterDTO.getUserRole());
        user.setUserAddress(userRegisterDTO.getUserAddress());
        userRepository.save(user);
    }

    // fetch user profile
    public UserResponseDTO getUserProfile() {
        // get the authentication object from security context
        String userEmail = validateUserAuthentication();

        User user = userRepository.findByUserEmail(userEmail);

        if (user == null) {
            throw new RuntimeException("User not found with email: " + userEmail);
        }

        return new UserResponseDTO(
                user.getUserId(),
                user.getUserName(),
                user.getUserEmail(),
                user.getUserPhone(),
                user.getUserAddress(),
                user.getUserRole());
    }

    // update user profile
    public UpdateResponseDTO updateUserProfile(UpdateResponseDTO updateResponseDTO) {
        String userEmail = validateUserAuthentication();

        User user = userRepository.findByUserEmail(userEmail);

        if (user == null) {
            throw new RuntimeException("User not found with email: " + userEmail);
        }

        if (user.getUserName() != null)
            user.setUserName(updateResponseDTO.getUserName());
        if (updateResponseDTO.getUserPassword() != null)
            user.setUserPassword(passwordEncoder.encode(updateResponseDTO.getUserPassword()));
        if (updateResponseDTO.getUserPhone() != null)
            user.setUserPhone(updateResponseDTO.getUserPhone());
        if (updateResponseDTO.getUserAddress() != null)
            user.setUserAddress(updateResponseDTO.getUserAddress());
        userRepository.save(user);

        return new UpdateResponseDTO(
                user.getUserName(),
                updateResponseDTO.getUserPassword() != null ? updateResponseDTO.getUserPassword()
                        : user.getUserPassword(),
                user.getUserPhone(),
                user.getUserAddress());
    }

}

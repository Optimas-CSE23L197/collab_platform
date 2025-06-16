package com.example.collaboration.collab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.collaboration.collab.config.CustomEmployeeDetails;
import com.example.collaboration.collab.config.CustomUserDetails;
import com.example.collaboration.collab.model.Employee;
import com.example.collaboration.collab.model.User;
import com.example.collaboration.collab.repository.EmployeeRepository;
import com.example.collaboration.collab.repository.UserRepository;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // this method load user and employee details based on user input
    // if username is email then it will load user details
    // if username is employee id then it will load employee details
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userName = username;

        if (userName.contains("@")) {
            User user = userRepository.findByUserEmail(userName);

            if (user == null) {
                throw new UsernameNotFoundException("User not found with email: " + userName);
            }

            return new CustomUserDetails(user);
        }

        Employee employee = employeeRepository.findByEmployeeId(userName);

        if (employee == null) {
            throw new UsernameNotFoundException("Employee not found with ID: " + userName);
        }

        return new CustomEmployeeDetails(employee);
    }

}

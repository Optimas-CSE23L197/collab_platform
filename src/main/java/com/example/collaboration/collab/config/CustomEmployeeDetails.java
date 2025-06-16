package com.example.collaboration.collab.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.collaboration.collab.model.Employee;

public class CustomEmployeeDetails extends UnifiedCustomAuth {

    private final String role;

    public CustomEmployeeDetails(Employee employee) {
        super(employee.getEmployeeId(), employee.getEmployeePassword());
        this.role = employee.getEmployeeRole().toString();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

}

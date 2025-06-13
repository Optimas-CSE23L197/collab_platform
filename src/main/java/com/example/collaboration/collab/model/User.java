package com.example.collaboration.collab.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_email", unique = true, nullable = false)
    private String userEmail;

    @Column(name = "user_password", nullable = false)
    @JsonIgnore
    private String userPassword;

    @Column(name = "user_phone", nullable = false)
    private String userPhone;

    @Column(name = "user_address")
    private String userAddress;

    @Column(name = "user_role", nullable = false)
    private String userRole;
}

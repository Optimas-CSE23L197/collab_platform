package com.example.collaboration.collab.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @Column(name = "userId", nullable = false, unique = true)
    private String userId;

    @Column(name = "userName", nullable = false, length = 100)
    private String userName;

    @Column(name = "userEmail", nullable = false, unique = true, length = 100)
    private String userEmail;

    @Column(name = "userPhone", nullable = false, length = 15)
    private String userPhone;

    @Column(name = "userPassword", nullable = false)
    private String userPassword; // ensure hashed before saving

    @Column(name = "userAddress", nullable = true, length = 255)
    private String userAddress;
}

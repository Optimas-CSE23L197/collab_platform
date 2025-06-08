package com.example.collaboration.collab.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "complaints")
@Getter
@Setter
public class Complaint {

    @Id
    @Column(name = "complaintId", nullable = false, unique = true)
    private String complaintId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Refers to the user who created the complaint

    private String deptId;

    @Column(name = "complaintName", nullable = false, length = 100)
    private String complaintName;

    @Column(name = "complaintDescription", nullable = false, length = 500)
    private String complaintDescription;

    @Column(name = "complaintStatus", nullable = false, length = 50)
    private String complaintStatus;

    @Column(name = "complaintDateAt")
    private String complaintDateAt;

    @Column(name = "complaintResolveAt")
    private String complaintResolveAt;

    @Column(name = "employeeName", length = 100)
    private String employeeName;
}

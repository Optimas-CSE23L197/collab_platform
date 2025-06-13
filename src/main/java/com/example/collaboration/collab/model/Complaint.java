package com.example.collaboration.collab.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "complaints")
public class Complaint {

    @Id
    private String complaintId;

    private String complaintTitle;
    private String complaintDescription;

    @Enumerated(EnumType.STRING)
    private ComplaintStatus complaintStatus;

    @Enumerated(EnumType.STRING)
    private ComplaintPriority complaintPriority;

    private LocalDateTime complaintCreatedAt;
    private LocalDateTime complaintUpdatedAt;
    private LocalDateTime complaintResolvedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String departmentId;

    public enum ComplaintStatus {
        PENDING,
        IN_PROGRESS,
        RESOLVED,
        REJECTED
    }

    public enum ComplaintPriority {
        LOW,
        MEDIUM,
        HIGH
    }
}

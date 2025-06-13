package com.example.collaboration.collab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ComplaintRequestDTO {

    @NotBlank(message = "Complaint title is required")
    private String complaintTitle;

    @NotBlank(message = "Complaint description is required")
    private String complaintDescription;

    @NotBlank(message = "Complaint priority is required")
    private String complaintPriority;

    @NotBlank(message = "Department ID is required")
    private String departmentId;
}

package com.example.collaboration.collab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ComplaintResponseDTO {

    @NotBlank(message = "complaint id must not black")
    private String complaintId;

    private String userName;
    private String userId;
    private String deptId;
    private String complaintName;
    private String complaintDescription;
    private String complaintStatus;
    private String complaintDateAt;
    private String complaintResolveAt;
    private String employeeName;
}

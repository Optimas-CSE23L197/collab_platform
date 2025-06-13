package com.example.collaboration.collab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ComplaintUpdateRequest {
    private String complaintId;
    private String complaintTitle;
    private String complaintDescription;
    private String departmentId;
}

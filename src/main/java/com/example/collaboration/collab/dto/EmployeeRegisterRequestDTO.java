package com.example.collaboration.collab.dto;

import com.example.collaboration.collab.model.Request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeRegisterRequestDTO {
    private String employeeName;
    private String employeeEmail;
    private String employeePhone;
    private String employeePassword;
    private String employeeRole;
    private String departmentId;
    private String approverId;
    private Request.RequestType requestType;
}

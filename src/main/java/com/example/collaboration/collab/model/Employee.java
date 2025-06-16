package com.example.collaboration.collab.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Employee {
    @Id
    @Column(name = "employee_id", nullable = false, unique = true)
    private String employeeId;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @Column(name = "employee_email", nullable = false, unique = true)
    private String employeeEmail;

    @Column(name = "employee_phone", nullable = false, unique = true)
    private String employeePhone;

    @Column(name = "employee_password", nullable = false)
    private String employeePassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_role", nullable = false)
    private EmployeeRole employeeRole;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    public enum EmployeeRole {
        HOD,
        CLERK,
        EMPLOYEE
    }
}

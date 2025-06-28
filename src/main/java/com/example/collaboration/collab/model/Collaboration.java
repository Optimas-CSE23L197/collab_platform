package com.example.collaboration.collab.model;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Collaboration {
    @Id
    @Column(name = "collaboration_id", nullable = false, unique = true)
    private String roomId;

    @Column(name = "collaboration_password", nullable = false)
    private String roomPassword;

    @Column(name = "create_by", nullable = false)
    private String createBy;

    @Column(name = "creator_department", nullable = false)
    private String creatorDepartment;

    @ManyToMany
    @JoinTable(name = "collaboration_departments", joinColumns = @JoinColumn(name = "collaboration_id"), inverseJoinColumns = @JoinColumn(name = "dept_id"))
    private Set<Department> departments;

    @ManyToMany
    @JoinTable(name = "collaboration_employees", joinColumns = @JoinColumn(name = "collaboration_id"), inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private Set<Employee> employees;

    private LocalDate createdAt;
    private LocalDate disbandAt;
}

package com.example.collaboration.collab.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Department {
    @Id
    @Column(name = "dept_id", nullable = false, unique = true)
    private String deptId;

    @Column(name = "dept_name", nullable = false)
    private String deptName;

    @ManyToMany(mappedBy = "departments") // ← must match the name on Collaboration
    private Set<Collaboration> collaborations;
}

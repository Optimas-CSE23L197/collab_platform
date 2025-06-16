package com.example.collaboration.collab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.collaboration.collab.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    Employee findByEmployeeId(String employeeId);

    List<Employee> findByDepartment_deptId(String deptID);

}

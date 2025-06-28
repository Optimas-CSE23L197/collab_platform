package com.example.collaboration.collab.service;

import java.time.LocalDate;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.collaboration.collab.dto.RoomJoinRequestDTO;
import com.example.collaboration.collab.dto.RoomRequestDTO;
import com.example.collaboration.collab.model.Collaboration;
import com.example.collaboration.collab.model.Department;
import com.example.collaboration.collab.model.Employee;
import com.example.collaboration.collab.model.Employee.EmployeeRole;
import com.example.collaboration.collab.repository.CollaborationRepository;
import com.example.collaboration.collab.repository.EmployeeRepository;

@Service
public class CollaborationService {

    @Autowired
    private CollaborationRepository collaborationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String generateRoomID() {
        String name = "ROOM";
        Random rand = new Random();

        return name + String.format("%05d", rand.nextInt(9999));
    }

    // create new room
    @PreAuthorize("hasAnyRole('HOD','CLERK')")
    public void createRoom(RoomRequestDTO roomRequestDTO) {
        String empId = employeeService.validateEmployeeAuthentication();

        Employee employee = employeeRepository.findByEmployeeId(empId);

        if (employee == null)
            throw new RuntimeException("Employee not found with id " + empId);

        if (employee.getEmployeeRole() != EmployeeRole.CLERK && employee.getEmployeeRole() != EmployeeRole.HOD) {
            throw new RuntimeException("Not valid role");
        }

        Collaboration newCollab = new Collaboration();
        newCollab.setRoomId(generateRoomID());
        newCollab.setRoomPassword(passwordEncoder.encode(roomRequestDTO.getRoomPassword()));
        newCollab.setCreateBy(employee.getEmployeeId());
        newCollab.setCreatorDepartment(employee.getDepartment().getDeptId());
        newCollab.setCreatedAt(LocalDate.now());
        collaborationRepository.save(newCollab);
    }

    // employee who are joining room
    public void joinRoom(RoomJoinRequestDTO roomJoinRequestDTO) {
        String empId = employeeService.validateEmployeeAuthentication();

        Employee employee = employeeRepository.findByEmployeeId(empId);

        if (employee == null)
            throw new RuntimeException("Employee not found");

        Collaboration collaboration = collaborationRepository.findByRoomId(roomJoinRequestDTO.getRoomId());

        if (collaboration == null)
            throw new RuntimeException("Room id not found");

        if (!passwordEncoder.matches(roomJoinRequestDTO.getRoomPassword(), collaboration.getRoomPassword()))
            throw new RuntimeException("Invalid Crediatntials");

        Department empDept = employee.getDepartment();

        if (!collaboration.getDepartments().contains(empDept)) {
            throw new RuntimeException("Employee department is not present");
        }
        collaboration.getEmployees().add(employee);
        collaborationRepository.save(collaboration);
    }

    // department joining logic
    @PreAuthorize("hasAnyRole('HOD','CLERK')")
    public void joinDepartment(String roomId) {
        String empId = employeeService.validateEmployeeAuthentication();

        Employee employee = employeeRepository.findByEmployeeId(empId);

        if (employee == null)
            throw new RuntimeException("Employee not found");

        Department empDept = employee.getDepartment();

        Collaboration collaboration = collaborationRepository.findByRoomId(roomId);

        // accept logic will add on frontend
        collaboration.getDepartments().add(empDept);
        collaborationRepository.save(collaboration);
    }
}

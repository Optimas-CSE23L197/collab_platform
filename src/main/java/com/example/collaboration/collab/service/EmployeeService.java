package com.example.collaboration.collab.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.collaboration.collab.dto.EmployeeApprovedDTO;
import com.example.collaboration.collab.dto.EmployeeRegisterRequestDTO;
import com.example.collaboration.collab.model.Employee;
import com.example.collaboration.collab.model.Request;
import com.example.collaboration.collab.model.Employee.EmployeeRole;
import com.example.collaboration.collab.model.Request.RequestStatus;
import com.example.collaboration.collab.model.Request.RequestType;
import com.example.collaboration.collab.repository.EmployeeRepository;
import com.example.collaboration.collab.repository.RequestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestService requestService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // check if employee is authenticated
    public String validateEmployeeAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Employee is not authenticated");
        }

        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    // generate employee id
    public String generateEmployeeId() {
        String namePart = "EMP";
        Random random = new Random();

        return namePart + String.format("%04d", random.nextInt(10000));
    }

    // check employee existance by employee id
    public Employee getEmployeeById(String employeeId) {
        employeeId = validateEmployeeAuthentication();

        Employee employee = employeeRepository.findByEmployeeId(employeeId);

        if (employee == null) {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }

        return employee;
    }

    // get all employees in a department
    public List<Employee> getAllEmployees(String deptId) {
        return employeeRepository.findByDepartment_deptId(deptId);
    }

    // register a new employee
    public void registerEmployee(EmployeeRegisterRequestDTO employeeRegisterRequestDTO) throws JsonProcessingException {
        // validate employee authentication
        String empId = validateEmployeeAuthentication();

        Employee employee = getEmployeeById(empId);

        if (employee == null) {
            throw new RuntimeException("Employee not found with id: " + empId);
        }

        if (employee.getEmployeeRole() != EmployeeRole.CLERK) {
            throw new RuntimeException("Only clerk can register a new employee");
        }

        Request request = new Request();
        try {
            request.setRequestId(requestService.generateRequestId());
        } catch (Exception e) {
            throw new RuntimeException("Error generating request ID: " + e.getMessage());
        }

        request.setRequestType(employeeRegisterRequestDTO.getRequestType());
        ObjectMapper mapper = new ObjectMapper();
        String jsonData = mapper.writeValueAsString(employeeRegisterRequestDTO);
        request.setRequestData(jsonData);
        request.setRequestStatus(Request.RequestStatus.Pending);
        request.setRequestBy(employee.getEmployeeId());
        request.setApprovedBy(employeeRegisterRequestDTO.getApproverId());
        request.setDepartmentId(employee.getDepartment().getDeptId());
        request.setRequestDate(LocalDate.now());
        request.setApprovalDate(null);
        requestRepository.save(request);

    }

    // get employee for register by hod
    public Request getEmployeeForRegister(String requestId) throws JsonMappingException, JsonProcessingException {
        String employeeId = validateEmployeeAuthentication();
        Employee employee = getEmployeeById(employeeId);
        if (employee == null) {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }

        if (employee.getEmployeeRole() != EmployeeRole.HOD) {
            throw new RuntimeException("Only HOD can view employee registration requests");
        }

        Optional<Request> requestOptional = requestRepository.findByRequestId(requestId);
        if (requestOptional.isEmpty()) {
            throw new RuntimeException("Request not found with id: " + requestId);
        }

        Request request = requestOptional.get();
        if (request.getRequestStatus() != Request.RequestStatus.Pending) {
            throw new RuntimeException("Request is not in pending state");
        }

        ObjectMapper mapper = new ObjectMapper();
        EmployeeRegisterRequestDTO employeeRegisterRequestDTO = mapper.readValue(request.getRequestData(),
                EmployeeRegisterRequestDTO.class);
        System.out.println(request.getRequestType().toString());

        return new Request(
                request.getRequestId(),
                RequestType.valueOf(request.getRequestType().toString()),
                request.getRequestData(),
                request.getRequestStatus(),
                request.getRequestBy(),
                request.getApprovedBy(),
                request.getDepartmentId(),
                request.getRequestDate(),
                request.getApprovalDate());
    }

    // higher authroty approval for employee registration
    // only for department head
    @Transactional
    public void approveEmployeeRegistration(String requestId, EmployeeApprovedDTO employeeApprovedRequestDTO)
            throws JsonMappingException, JsonProcessingException {
        String employeeId = validateEmployeeAuthentication();

        Employee employee = getEmployeeById(employeeId);

        if (employee == null) {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }

        if (employee.getEmployeeRole() != EmployeeRole.HOD) {
            throw new RuntimeException("Only HOD can approve employee registration");
        }

        Optional<Request> requestOptional = requestRepository.findByRequestId(requestId);
        if (requestOptional.isEmpty()) {
            throw new RuntimeException("Request not found with id: " + requestId);
        }

        Request request = requestOptional.get();

        if (request.getRequestStatus() != Request.RequestStatus.Pending) {
            throw new RuntimeException("Request is not in pending state");
        }

        String status = employeeApprovedRequestDTO.getRequestStatus();

        if ("accepted".equalsIgnoreCase(status)) {
            request.setRequestStatus(Request.RequestStatus.Approved);
        } else if ("rejected".equalsIgnoreCase(status)) {
            request.setRequestStatus(Request.RequestStatus.Rejected);
        } else {
            throw new RuntimeException("Invalid request status: " + status);
        }

        request.setApprovedBy(employeeId);
        request.setApprovalDate(LocalDate.now());

        requestRepository.save(request);

        ObjectMapper mapper = new ObjectMapper();
        EmployeeRegisterRequestDTO employeeRegisterRequestDTO = mapper.readValue(request.getRequestData(),
                EmployeeRegisterRequestDTO.class);

        Employee newEmployee = new Employee();
        newEmployee.setEmployeeId(generateEmployeeId());
        newEmployee.setEmployeeName(employeeRegisterRequestDTO.getEmployeeName());
        newEmployee.setEmployeeEmail(employeeRegisterRequestDTO.getEmployeeEmail());
        newEmployee.setEmployeePhone(employeeRegisterRequestDTO.getEmployeePhone());
        newEmployee.setEmployeePassword(passwordEncoder.encode(employeeRegisterRequestDTO.getEmployeePassword()));
        newEmployee.setEmployeeRole(EmployeeRole.valueOf(employeeRegisterRequestDTO.getEmployeeRole().toUpperCase()));
        newEmployee.setDepartment(employee.getDepartment());

        if (request.getRequestStatus() != RequestStatus.Approved) {
            throw new RuntimeException("Request is not approved");
        }

        employeeRepository.save(newEmployee);
    }

}

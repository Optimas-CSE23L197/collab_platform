package com.example.collaboration.collab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collaboration.collab.dto.ApiResponseDTO;
import com.example.collaboration.collab.dto.EmployeeApprovedDTO;
import com.example.collaboration.collab.dto.EmployeeRegisterRequestDTO;
import com.example.collaboration.collab.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/employee")
// @PreAuthorize("hasAnyRole('ROLE_HOD', 'ROLE_CLERK', 'ROLE_EMPLOYEE')")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // get all employees in a department
    @GetMapping("/all/{deptId}")
    public ResponseEntity<ApiResponseDTO> getAllEmployees(@PathVariable String deptId) {
        var employees = employeeService.getAllEmployees(deptId);

        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), null, "Employees fetch successfully",
                employees);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // register a new employee
    @PreAuthorize("hasRole('ROLE_CLERK')")
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO> registerEmployee(
            @RequestBody EmployeeRegisterRequestDTO employeeRegisterRequestDTO) {
        try {
            employeeService.registerEmployee(employeeRegisterRequestDTO);
        } catch (JsonProcessingException e) {
            ApiResponseDTO errorResponse = new ApiResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), null,
                    "Failed to process request data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), null,
                "Request sent to higher authority for approval",
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get employee for register by hod
    @PreAuthorize("hasRole('ROLE_HOD')")
    @GetMapping("/request/{requestId}")
    public ResponseEntity<ApiResponseDTO> getEmployeeForRegister(@PathVariable String requestId)
            throws JsonMappingException, JsonProcessingException {
        var employee = employeeService.getEmployeeForRegister(requestId);
        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), null, "Employee fetched successfully",
                employee);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // approve employee registration by hod
    @PreAuthorize("hasRole('ROLE_HOD')")
    @PatchMapping("/approve/{requestId}")
    public ResponseEntity<ApiResponseDTO> approveEmployeeRegistration(@PathVariable String requestId,
            @RequestBody EmployeeApprovedDTO employeeApprovedRequestDTO)
            throws JsonMappingException, JsonProcessingException {
        try {
            employeeService.approveEmployeeRegistration(requestId,
                    employeeApprovedRequestDTO);
        } catch (JsonProcessingException e) {
            ApiResponseDTO errorResponse = new ApiResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), null,
                    "Failed to process request data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        ApiResponseDTO response = new ApiResponseDTO(HttpStatus.OK.value(), null,
                "Employee registration approved",
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

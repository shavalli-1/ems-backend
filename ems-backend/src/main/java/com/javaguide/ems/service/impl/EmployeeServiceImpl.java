package com.javaguide.ems.service.impl;

import com.javaguide.ems.dto.EmployeeDto;
import com.javaguide.ems.entity.Employee;
import com.javaguide.ems.exception.ResourceNotFoundException;
import com.javaguide.ems.mapper.EmployeeMapper;
import com.javaguide.ems.repository.EmployeeRepository;
import com.javaguide.ems.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee Not exists with the given id : " + employeeId));
        return EmployeeMapper.mapToEmployeeDto(employee);
    }


    @GetMapping
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> listOfEmployees = employeeRepository.findAll();
        return listOfEmployees.stream().map((employee) -> EmployeeMapper.mapToEmployeeDto(employee)).collect(Collectors.toList());
    }

    @PostMapping("/{id}")
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployee) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee is not exist with the given id : " + employeeId));

        // Validate the updatedEmployee object (optional but recommended)
        if (updatedEmployee.getEmail() == null || updatedEmployee.getFirstName() == null || updatedEmployee.getLastName() == null) {
            throw new IllegalArgumentException("Invalid data provided for update");
        }

        employee.setEmail(updatedEmployee.getEmail());
        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());

        Employee updatedEmployeeObj = employeeRepository.save(employee);

        return EmployeeMapper.mapToEmployeeDto(updatedEmployeeObj);
    }

    @Override
    public String deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee doesn't exist with the given id : " + employeeId));
        employeeRepository.deleteById(employeeId);
        return "Deleted the Record Successfully!";
    }
}

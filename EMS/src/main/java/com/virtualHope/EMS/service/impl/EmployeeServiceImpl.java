package com.virtualHope.EMS.service.impl;

import com.virtualHope.EMS.dto.EmployeeUpdateDTO;
import com.virtualHope.EMS.exception.ResourceNotFoundException;
import com.virtualHope.EMS.model.Employee;
import com.virtualHope.EMS.repository.EmployeeRepository;
import com.virtualHope.EMS.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Employee saveEmployee(Employee employee) {
        return repository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public Employee updateEmployee(Long id, EmployeeUpdateDTO employee) {
        // Fetch existing employee data
        Employee existing = getEmployeeById(id);
        // Update employee fields
        existing.setName(employee.getName());
        existing.setEmail(employee.getEmail());
        existing.setDepartment(employee.getDepartment());
        existing.setSalary(employee.getSalary());
        existing.setRole(employee.getRole());
        // Update manager based on manager_id
        if (employee.getManagerId() != null) {
            Employee manager = getEmployeeById(employee.getManagerId());
            existing.setManager(manager);
        } else {
            existing.setManager(null); // If no manager, set to null
        }

        // Save and return the updated employee
        return repository.save(existing);
    }


    @Override
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        repository.delete(employee);
    }

    @Override
    public Employee getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public Employee save(Employee emp) {

        if (repository.findByUsername(emp.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists. Please use a different one.");
        }

        emp.setPassword(passwordEncoder.encode(emp.getPassword()));
        return repository.save(emp);
    }

    public List<Employee> getAllSubordinates(String username) {
        Employee manager = repository.findByUsername(username).orElseThrow();
        List<Employee> result = new ArrayList<>();
        Queue<Employee> queue = new LinkedList<>();
        queue.add(manager);

        while (!queue.isEmpty()) {
            Employee current = queue.poll();
            List<Employee> subs = repository.findByManager(current);
            result.addAll(subs);
            queue.addAll(subs);
        }

        return result;
    }



}
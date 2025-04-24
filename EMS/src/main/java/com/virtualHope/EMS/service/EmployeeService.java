package com.virtualHope.EMS.service;

import com.virtualHope.EMS.dto.EmployeeUpdateDTO;
import com.virtualHope.EMS.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    Employee updateEmployee(Long id, EmployeeUpdateDTO employee);
    void deleteEmployee(Long id);
    Employee getByUsername(String username);
    Employee save(Employee emp);
    List<Employee> getAllSubordinates(String username);
}

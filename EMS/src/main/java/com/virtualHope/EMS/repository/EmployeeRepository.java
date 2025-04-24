package com.virtualHope.EMS.repository;

import com.virtualHope.EMS.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // You can add custom query methods if needed
    Optional<Employee> findByUsername(String username);
    List<Employee> findByManager(Employee current);
}


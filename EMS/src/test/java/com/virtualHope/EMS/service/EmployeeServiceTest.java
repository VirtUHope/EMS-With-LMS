package com.virtualHope.EMS.service;

import com.virtualHope.EMS.model.Employee;
import com.virtualHope.EMS.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    void testSaveAndGetByUsername() {
        // Create a new employee object
        String uniqueUsername = "testuser_" + System.currentTimeMillis();

        Employee emp = Employee.builder()
                .username(uniqueUsername)
                .password("plainpassword123")
                .name("Test User")
                .email(uniqueUsername + "@example.com")
                .department("QA")
                .salary(50000)
                .role(Role.ASSOCIATE)
                .build();
        // Save the employee
        Employee saved = employeeService.save(emp);

        // Validate save was successful
        assertNotNull(saved.getId());

        // Fetch the employee by username
        Employee fetched = employeeService.getByUsername("testuser1");

        // Validate fields
        assertEquals("Test User", fetched.getName());
        assertEquals("testuser@example.com", fetched.getEmail());
        assertEquals("QA", fetched.getDepartment());
        assertEquals(50000, fetched.getSalary());
        assertEquals(Role.ASSOCIATE, fetched.getRole());
    }
}


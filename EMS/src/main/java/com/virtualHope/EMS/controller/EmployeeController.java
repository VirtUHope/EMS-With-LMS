package com.virtualHope.EMS.controller;

import com.virtualHope.EMS.controller.request.LoginRequest;
import com.virtualHope.EMS.dto.EmployeeUpdateDTO;
import com.virtualHope.EMS.model.Employee;
import com.virtualHope.EMS.service.EmployeeService;
import com.virtualHope.EMS.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Employee employee = service.getByUsername(loginRequest.getUsername());

        if (employee == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), employee.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtService.generateToken(employee.getUsername(), employee.getRole().name());
        return ResponseEntity.ok().body(Collections.singletonMap("token", "Bearer " + token));
    }



    @GetMapping("/data")
    public ResponseEntity<?> getEmployeeData(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(403).body(null); // Forbidden if no token
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        if ("ADMIN".equals(role)) {
            return ResponseEntity.ok(service.getAllEmployees());
        } else {
            return ResponseEntity.ok(service.getByUsername(username));
        }
    }



    @GetMapping("/profile")
    public ResponseEntity<Employee> getProfile(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(403).body(null); // Forbidden if no token
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        Employee employee = service.getByUsername(username);
        if (employee == null) {
            return ResponseEntity.status(404).body(null); // Not found if user not exist
        }

        return ResponseEntity.ok(employee);
    }





    @PostMapping("/register")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(service.save(employee));
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(service.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEmployeeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody EmployeeUpdateDTO employee) {
        return ResponseEntity.ok(service.updateEmployee(id, employee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        service.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/subordinates")
    public ResponseEntity<?> getSubordinates(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);

        List<Employee> subs = service.getAllSubordinates(username);
        return ResponseEntity.ok(subs);
    }


}

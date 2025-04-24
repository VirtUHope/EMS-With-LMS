package com.virtualHope.EMS.dto;

import com.virtualHope.EMS.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateDTO {
    private String name;
    private String email;
    private String department;
    private Double salary;
    private Role role;
    private Long managerId;
}


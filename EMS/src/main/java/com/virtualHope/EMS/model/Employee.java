package com.virtualHope.EMS.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;
    private String name;
    private String email;
    private String department;
    private double salary;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Manager: self-referencing
    @ManyToOne
    @JsonIgnoreProperties({"manager", "password", "email", "salary", "department", "role"}) // adjust as per your needs
    private Employee manager;

    private int totalPaidLeaves = 18; // default every year
    private int usedPaidLeaves = 0;   // gets incremented when applying

}


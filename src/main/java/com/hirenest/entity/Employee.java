
package com.hirenest.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String employeeId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private LocalDate dateOfBirth;

    private String residentialAddress;

    private String permanentAddress;

    private String emergencyContactName;

    private String emergencyContactPhone;

    @Column(nullable = false)
    private String status;

    private String employmentType;

    private LocalDate joiningDate;

    @ManyToOne
    @JoinColumn(name = "domain_id")
    private Domain domain;

    // Returns the domain name from the related Domain entity.
    @Transient
    public String getDomainName() {
        return domain != null ? domain.getName() : null;
    }
}

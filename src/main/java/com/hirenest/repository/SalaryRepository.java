package com.hirenest.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hirenest.entity.SalaryDetails;

public interface SalaryRepository extends JpaRepository<SalaryDetails, Long> {

    // all salary records of one employee (salary history)
    List<SalaryDetails> findByEmployeeId(String employeeId);

    // latest salary of one employee
    Optional<SalaryDetails> findTopByEmployeeIdOrderByEffectiveDateDesc(String employeeId);
}

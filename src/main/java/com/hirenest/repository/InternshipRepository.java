package com.hirenest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hirenest.entity.Employee;
import com.hirenest.entity.InternshipDetails;

public interface InternshipRepository extends JpaRepository<InternshipDetails, Long> {

    Optional<InternshipDetails> findByEmployee(Employee employee);

    boolean existsByEmployee(Employee employee);
}

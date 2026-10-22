
package com.hirenest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hirenest.dto.EmployeeDomainCount;
import com.hirenest.entity.Domain;

public interface DomainRepository extends JpaRepository<Domain, Long> {

    Optional<Domain> findByName(String name);

    @Query("""
        SELECT new com.hirenest.dto.EmployeeDomainCount(
            d.name,
            COUNT(e.id)
        )
        FROM Domain d
        LEFT JOIN d.employees e
        GROUP BY d.id, d.name
        ORDER BY d.name
    """)
    List<EmployeeDomainCount> countEmployeesByDomain();
}

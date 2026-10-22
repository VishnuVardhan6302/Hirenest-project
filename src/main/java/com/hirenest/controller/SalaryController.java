package com.hirenest.controller;


import java.util.List;

import com.hirenest.dto.SalaryRequest;
import com.hirenest.dto.SalaryResponse;
import com.hirenest.service.SalaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;   // ADD
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    private final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")                      // ADD
    @PostMapping
    public ResponseEntity<SalaryResponse> create(@RequestBody SalaryRequest request) {
        return new ResponseEntity<>(salaryService.createSalary(request), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")               // ADD
    @GetMapping
    public List<SalaryResponse> getAll() {
        return salaryService.getAllSalaries();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")               // ADD
    @GetMapping("/{id}")
    public SalaryResponse getById(@PathVariable Long id) {
        return salaryService.getSalaryById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")               // ADD
    @GetMapping("/employee/{employeeId}")
    public List<SalaryResponse> getHistory(@PathVariable String employeeId) {
        return salaryService.getSalaryHistoryByEmployeeId(employeeId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")               // ADD
    @GetMapping("/employee/{employeeId}/current")
    public SalaryResponse getCurrent(@PathVariable String employeeId) {
        return salaryService.getCurrentSalaryByEmployeeId(employeeId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")                      // ADD
    @PutMapping("/{id}")
    public SalaryResponse update(@PathVariable Long id, @RequestBody SalaryRequest request) {
        return salaryService.updateSalary(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")                              // ADD
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salaryService.deleteSalary(id);
        return ResponseEntity.noContent().build();
    }
}
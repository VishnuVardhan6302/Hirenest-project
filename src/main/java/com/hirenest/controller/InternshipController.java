package com.hirenest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;   // ADD THIS IMPORT
import org.springframework.web.bind.annotation.*;

import com.hirenest.dto.InternshipRequest;
import com.hirenest.dto.InternshipResponse;
import com.hirenest.service.InternshipService;

@RestController
@RequestMapping("/api/internship")
public class InternshipController {

    private final InternshipService internshipService;

    public InternshipController(InternshipService internshipService) {
        this.internshipService = internshipService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")                      // ADD
    @PostMapping
    public ResponseEntity<InternshipResponse> create(@RequestBody InternshipRequest request) {
        return new ResponseEntity<>(internshipService.createInternship(request), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")               // ADD
    @GetMapping
    public List<InternshipResponse> getAll() {
        return internshipService.getAllInternships();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")               // ADD
    @GetMapping("/{id}")
    public InternshipResponse getById(@PathVariable Long id) {
        return internshipService.getInternshipById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")               // ADD
    @GetMapping("/employee/{employeeId}")
    public InternshipResponse getByEmployee(@PathVariable String employeeId) {
        return internshipService.getInternshipByEmployeeId(employeeId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")                      // ADD
    @PutMapping("/{id}")
    public InternshipResponse update(@PathVariable Long id, @RequestBody InternshipRequest request) {
        return internshipService.updateInternship(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")                              // ADD
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        internshipService.deleteInternship(id);
        return ResponseEntity.noContent().build();
    }
}

package com.hirenest.controller;

import com.hirenest.dto.PfDetailsDto;
import com.hirenest.dto.PfDetailsResponse;
import com.hirenest.entity.PFDetails;
import com.hirenest.service.PfDetailsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pf")
@RequiredArgsConstructor
public class PfDetailsController {

    private final PfDetailsService service;

    // Create PF details - Employee only
    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PFDetails> createPfDetails(
            @RequestBody PfDetailsDto request,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                service.createPfDetails(request, email)
        );
    }

    // Get all PF details - HR and Admin
    @GetMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<List<PfDetailsResponse>> getAllPfDetails() {

        List<PfDetailsResponse> response =
                service.getAllPfDetails()
                        .stream()
                        .map(PfDetailsResponse::from)
                        .toList();

        return ResponseEntity.ok(response);
    }

    // Get PF details by employee ID - HR and Admin
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<PFDetails> getByEmployeeId(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                service.getByEmployeeId(employeeId)
        );
    }

    // Delete PF details - Admin only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePfDetails(
            @PathVariable Long id) {

        service.deletePfDetails(id);

        return ResponseEntity.noContent().build();
    }
}

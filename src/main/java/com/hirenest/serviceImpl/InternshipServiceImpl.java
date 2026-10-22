
package com.hirenest.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hirenest.dto.InternshipRequest;
import com.hirenest.dto.InternshipResponse;
import com.hirenest.entity.Employee;
import com.hirenest.entity.InternshipDetails;
import com.hirenest.exception.BadRequestException;
import com.hirenest.exception.ResourceNotFoundException;
import com.hirenest.repository.EmployeeRepository;
import com.hirenest.repository.InternshipRepository;
import com.hirenest.service.InternshipService;

@Service
public class InternshipServiceImpl implements InternshipService {

    private final InternshipRepository internshipRepository;
    private final EmployeeRepository employeeRepository;

    public InternshipServiceImpl(InternshipRepository internshipRepository,
                                 EmployeeRepository employeeRepository) {
        this.internshipRepository = internshipRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public InternshipResponse createInternship(InternshipRequest request) {
        Employee employee = employeeRepository.findByEmployeeId(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found: " + request.getEmployeeId()));

        if (internshipRepository.existsByEmployee(employee)) {
            throw new BadRequestException("Internship already exists for this employee");
        }
        validateDates(request);

        InternshipDetails internship = new InternshipDetails();
        internship.setEmployee(employee);
        mapRequestToEntity(request, internship);
        return toResponse(internshipRepository.save(internship));
    }

    @Override
    public InternshipResponse getInternshipById(Long id) {
        return toResponse(findInternship(id));
    }

    @Override
    public List<InternshipResponse> getAllInternships() {
        return internshipRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public InternshipResponse getInternshipByEmployeeId(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + employeeId));
        InternshipDetails internship = internshipRepository.findByEmployee(employee)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No internship found for employee: " + employeeId));
        return toResponse(internship);
    }

    @Override
    public InternshipResponse updateInternship(Long id, InternshipRequest request) {
        InternshipDetails internship = findInternship(id);
        validateDates(request);
        mapRequestToEntity(request, internship);
        return toResponse(internshipRepository.save(internship));
    }

    @Override
    public void deleteInternship(Long id) {
        internshipRepository.delete(findInternship(id));
    }

    private InternshipDetails findInternship(Long id) {
        return internshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Internship not found: " + id));
    }

    private void validateDates(InternshipRequest r) {
        if (r.getEndDate() != null && r.getEndDate().isBefore(r.getStartDate())) {
            throw new BadRequestException("End date cannot be before start date");
        }
    }

    private void mapRequestToEntity(InternshipRequest r, InternshipDetails i) {
        i.setStipend(r.getStipend());
        i.setFrequency(r.getFrequency());
        i.setStartDate(r.getStartDate());
        i.setEndDate(r.getEndDate());
    }

    private InternshipResponse toResponse(InternshipDetails i) {
        return new InternshipResponse(
                i.getId(),
                i.getEmployee().getEmployeeId(),
                i.getStipend(),
                i.getFrequency(),
                i.getStartDate(),
                i.getEndDate());
    }
}
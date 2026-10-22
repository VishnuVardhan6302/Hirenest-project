package com.hirenest.serviceImpl;

import java.util.List;

import com.hirenest.dto.SalaryRequest;
import com.hirenest.dto.SalaryResponse;
import com.hirenest.exception.ResourceNotFoundException;
import com.hirenest.repository.EmployeeRepository;
import com.hirenest.repository.SalaryRepository;
import com.hirenest.service.SalaryService;
import org.springframework.stereotype.Service;

import com.hirenest.entity.Employee;
import com.hirenest.entity.SalaryDetails;

@Service
public class SalaryServiceImpl implements SalaryService {

    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;

    public SalaryServiceImpl(SalaryRepository salaryRepository,
                             EmployeeRepository employeeRepository) {
        this.salaryRepository = salaryRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public SalaryResponse createSalary(SalaryRequest request) {
        Employee employee = employeeRepository.findByEmployeeId(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found: " + request.getEmployeeId()));

        SalaryDetails salary = new SalaryDetails();
        salary.setEmployee(employee);
        mapRequestToEntity(request, salary);
        return toResponse(salaryRepository.save(salary));
    }

    @Override
    public SalaryResponse getSalaryById(Long id) {
        return toResponse(findSalary(id));
    }

    @Override
    public List<SalaryResponse> getAllSalaries() {
        return salaryRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public List<SalaryResponse> getSalaryHistoryByEmployeeId(String employeeId) {
        return salaryRepository.findByEmployeeId(employeeId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public SalaryResponse getCurrentSalaryByEmployeeId(String employeeId) {
        SalaryDetails salary = salaryRepository
                .findTopByEmployeeIdOrderByEffectiveDateDesc(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No salary found for employee: " + employeeId));
        return toResponse(salary);
    }

    @Override
    public SalaryResponse updateSalary(Long id, SalaryRequest request) {
        SalaryDetails salary = findSalary(id);
        mapRequestToEntity(request, salary);
        return toResponse(salaryRepository.save(salary));
    }

    @Override
    public void deleteSalary(Long id) {
        salaryRepository.delete(findSalary(id));
    }

    // ---------- helpers ----------
    private SalaryDetails findSalary(Long id) {
        return salaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary not found: " + id));
    }

    private void mapRequestToEntity(SalaryRequest r, SalaryDetails s) {
        s.setSalary(r.getSalary());
        s.setFrequency(r.getFrequency());
        s.setEffectiveDate(r.getEffectiveDate());
        s.setCurrency(r.getCurrency());
    }

    private SalaryResponse toResponse(SalaryDetails s) {
        return new SalaryResponse(
                s.getId(),
                s.getEmployee().getEmployeeId(),
                s.getSalary(),
                s.getFrequency(),
                s.getEffectiveDate(),
                s.getCurrency());
    }
}

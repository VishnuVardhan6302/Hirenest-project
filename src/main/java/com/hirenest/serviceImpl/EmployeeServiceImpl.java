package com.hirenest.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hirenest.dto.EmployeeRequest;
import com.hirenest.dto.EmployeeResponse;
import com.hirenest.entity.Employee;
import com.hirenest.exception.DuplicateResourceException;
import com.hirenest.exception.ResourceNotFoundException;
import com.hirenest.repository.EmployeeRepository;
import com.hirenest.service.EmployeeService;
import com.hirenest.entity.Domain;
import com.hirenest.repository.DomainRepository;
import com.hirenest.entity.EmploymentDetails;
import com.hirenest.repository.EmploymentDetailsRepository;
import org.springframework.transaction.annotation.Transactional;
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DomainRepository domainRepository;
    private final EmploymentDetailsRepository employmentDetailsRepository;

    public EmployeeServiceImpl(
        EmployeeRepository employeeRepository,
        DomainRepository domainRepository,
        EmploymentDetailsRepository employmentDetailsRepository) {

    this.employeeRepository = employeeRepository;
    this.domainRepository = domainRepository;
    this.employmentDetailsRepository = employmentDetailsRepository;
}

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {

        if (employeeRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new DuplicateResourceException(
                    "Employee ID already exists: "
                    + request.getEmployeeId()
            );
        }

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Email already exists: "
                    + request.getEmail()
            );
        }

        Employee employee = new Employee();

        employee.setEmployeeId(request.getEmployeeId());
        employee.setFullName(request.getFullName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setResidentialAddress(request.getResidentialAddress());
        employee.setPermanentAddress(request.getPermanentAddress());
        employee.setEmergencyContactName(request.getEmergencyContactName());
        employee.setEmergencyContactPhone(request.getEmergencyContactPhone());
        employee.setEmploymentType(request.getEmploymentType());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setStatus(request.getStatus());

        Domain domain = domainRepository.findByName(request.getDomainName())
                .orElseThrow(() ->
                    new RuntimeException(
                        "Domain not found: " + request.getDomainName()
                    )
                );

        employee.setDomain(domain);

        Employee savedEmployee = employeeRepository.save(employee);

EmploymentDetails details = new EmploymentDetails();
details.setEmployee(savedEmployee);
details.setEmploymentType(savedEmployee.getEmploymentType());
details.setDomain(domain);
details.setJoiningDate(savedEmployee.getJoiningDate());
details.setStatus(savedEmployee.getStatus());

employmentDetailsRepository.save(details);

return convertToResponse(savedEmployee);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findByStatusIn(List.of("ACTIVE", "INACTIVE"))
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public List<EmployeeResponse> getFormerEmployees() {

        return employeeRepository.findByStatus("FORMER")
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Employee not found with id: " + id
                    )
                );

        return convertToResponse(employee);
    }

    @Override
    public EmployeeResponse updateEmployee(
            Long id,
            EmployeeRequest request) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Employee not found with id: " + id
                    )
                );

        employee.setFullName(request.getFullName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setResidentialAddress(request.getResidentialAddress());
        employee.setPermanentAddress(request.getPermanentAddress());
        employee.setEmergencyContactName(request.getEmergencyContactName());
        employee.setEmergencyContactPhone(request.getEmergencyContactPhone());

        Domain domain = domainRepository.findByName(request.getDomainName())
                .orElseThrow(() ->
                    new RuntimeException(
                        "Domain not found: " + request.getDomainName()
                    )
                );

        employee.setDomain(domain);

        employee.setEmploymentType(request.getEmploymentType());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setStatus(request.getStatus());

        Employee updatedEmployee = employeeRepository.save(employee);

EmploymentDetails details = employmentDetailsRepository
        .findAll()
        .stream()
        .filter(item -> item.getEmployee().getId()
                .equals(updatedEmployee.getId()))
        .findFirst()
        .orElseGet(EmploymentDetails::new);

details.setEmployee(updatedEmployee);
details.setEmploymentType(updatedEmployee.getEmploymentType());
details.setDomain(domain);
details.setJoiningDate(updatedEmployee.getJoiningDate());
details.setStatus(updatedEmployee.getStatus());

employmentDetailsRepository.save(details);

return convertToResponse(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Employee not found with id: " + id
                    )
                );

        // Instead of permanently deleting the employee,
        // change the status to FORMER.
        employee.setStatus("FORMER");

        employeeRepository.save(employee);
    }
    
    @Override
    public List<EmployeeResponse> getInternEmployees() {

        return employeeRepository
                .findByEmploymentTypeAndStatusIn(
                        "INTERN",
                        List.of("ACTIVE", "INACTIVE")
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }
    private EmployeeResponse convertToResponse(Employee employee) {

        EmployeeResponse response = new EmployeeResponse();

        response.setId(employee.getId());
        response.setEmployeeId(employee.getEmployeeId());
        response.setFullName(employee.getFullName());
        response.setEmail(employee.getEmail());
        response.setPhone(employee.getPhone());
        response.setDateOfBirth(employee.getDateOfBirth());
        response.setResidentialAddress(employee.getResidentialAddress());
        response.setPermanentAddress(employee.getPermanentAddress());
        response.setEmergencyContactName(employee.getEmergencyContactName());
        response.setEmergencyContactPhone(employee.getEmergencyContactPhone());
        response.setDomainName(employee.getDomainName());
        response.setEmploymentType(employee.getEmploymentType());
        response.setJoiningDate(employee.getJoiningDate());
        response.setStatus(employee.getStatus());

        return response;
    }
}
package com.hirenest.service;


import com.hirenest.dto.SalaryRequest;
import com.hirenest.dto.SalaryResponse;

import java.util.List;

public interface SalaryService {

    SalaryResponse createSalary(SalaryRequest request);

    SalaryResponse getSalaryById(Long id);

    List<SalaryResponse> getAllSalaries();

    List<SalaryResponse> getSalaryHistoryByEmployeeId(String employeeId);

    SalaryResponse getCurrentSalaryByEmployeeId(String employeeId);

    SalaryResponse updateSalary(Long id, SalaryRequest request);

    void deleteSalary(Long id);
}

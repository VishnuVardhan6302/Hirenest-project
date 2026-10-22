package com.hirenest.service;

import java.util.List;

import com.hirenest.dto.InternshipRequest;
import com.hirenest.dto.InternshipResponse;

public interface InternshipService {

    InternshipResponse createInternship(InternshipRequest request);

    InternshipResponse getInternshipById(Long id);

    List<InternshipResponse> getAllInternships();

    InternshipResponse getInternshipByEmployeeId(String employeeId);

    InternshipResponse updateInternship(Long id, InternshipRequest request);

    void deleteInternship(Long id);
}

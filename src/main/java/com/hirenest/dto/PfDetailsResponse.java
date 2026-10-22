
package com.hirenest.dto;

import com.hirenest.entity.PFDetails;

public record PfDetailsResponse(
        Long id,
        Long employeeId,
        String employeeCode,
        String employeeName,
        String employeeEmail,
        String pfAccountNumber,
        String uan,
        String pfStatus,
        String uanStatus
) {
    public static PfDetailsResponse from(PFDetails pf) {
        var employee = pf.getEmployee();

        return new PfDetailsResponse(
                pf.getId(),
                employee != null ? employee.getId() : null,
                employee != null ? employee.getEmployeeId() : null,
                employee != null ? employee.getFullName() : null,
                employee != null ? employee.getEmail() : null,
                pf.getPfAccountNumber(),
                pf.getUan(),
                pf.getPfStatus(),
                pf.getUanStatus()
        );
    }
}

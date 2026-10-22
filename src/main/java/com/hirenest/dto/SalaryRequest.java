package com.hirenest.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalaryRequest {

    private String employeeId;
    private BigDecimal salary;
    private String frequency;       // MONTHLY / YEARLY
    private LocalDate effectiveDate;
    private String currency;        // INR, USD ...
}

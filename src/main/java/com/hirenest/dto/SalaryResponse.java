package com.hirenest.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalaryResponse {

    private Long id;
    private String employeeId;
    private BigDecimal salary;
    private String frequency;
    private LocalDate effectiveDate;
    private String currency;
}
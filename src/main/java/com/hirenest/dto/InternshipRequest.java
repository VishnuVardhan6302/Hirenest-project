package com.hirenest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InternshipRequest {

    private String employeeId;
    private BigDecimal stipend;
    private String frequency;       // MONTHLY
    private LocalDate startDate;
    private LocalDate endDate;
}
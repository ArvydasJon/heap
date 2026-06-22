package com.arvydas.heap.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class WorkReportDto {

    private BigDecimal totalAmount1;
    private BigDecimal totalAmount2;
    private long entriesCount;
}


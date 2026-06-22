package com.arvydas.heap.dto;

import jakarta.persistence.Column;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;



//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data  //Data padengia getter ir setter

public class WorkEntryDto {

    private Long id;
    @NotNull
    private LocalDate date;
    @NotBlank
    private String object;
    @NotBlank
    private String work;
    @NotNull
    @DecimalMin("0.00")

    private BigDecimal amount1;

    private BigDecimal amount2;


    private String notes;
}


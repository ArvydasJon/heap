package com.arvydas.heap.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "work_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String object;

    private String work;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount1;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount2;

    @Column(length = 1000)
    private String notes;

    // ---------------- User ryšys ----------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
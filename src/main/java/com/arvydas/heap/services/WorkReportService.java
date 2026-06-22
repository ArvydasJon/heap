/*package com.arvydas.heap.services;

import com.arvydas.heap.dto.WorkReportDto;
import com.arvydas.heap.model.WorkEntry;
import com.arvydas.heap.repository.HeapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkReportService {

    private final HeapRepository repository;

    public WorkReportDto getReport(LocalDate from, LocalDate to) {

        List<WorkEntry> filtered = repository.findAll()
                .stream()
                .filter(entry ->
                        (from == null || !entry.getDate().isBefore(from)) &&
                                (to == null || !entry.getDate().isAfter(to))
                )
                .toList();

        BigDecimal total1 = filtered.stream()
                .map(e -> e.getAmount1() != null ? e.getAmount1() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal total2 = filtered.stream()
                .map(e -> e.getAmount2() != null ? e.getAmount2() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new WorkReportDto(total1, total2, filtered.size());
    }

    public BigDecimal getTotalAmount1(LocalDate from, LocalDate to) {
        return repository.findAll()
                .stream()
                .filter(e -> (from == null || !e.getDate().isBefore(from)) &&
                        (to == null || !e.getDate().isAfter(to)))
                .map(e -> e.getAmount1() != null ? e.getAmount1() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalAmount2(LocalDate from, LocalDate to) {
        return repository.findAll()
                .stream()
                .filter(e -> (from == null || !e.getDate().isBefore(from)) &&
                        (to == null || !e.getDate().isAfter(to)))
                .map(e -> e.getAmount2() != null ? e.getAmount2() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
*/
package com.arvydas.heap.services;

import com.arvydas.heap.dto.WorkReportDto;
import com.arvydas.heap.model.User;
import com.arvydas.heap.model.WorkEntry;
import com.arvydas.heap.repository.HeapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkReportService {

    private final HeapRepository repository;

    // ---------------- MAIN REPORT ----------------
    public WorkReportDto getReport(User user, LocalDate from, LocalDate to) {

        List<WorkEntry> filtered = getFilteredEntries(user, from, to);

        BigDecimal total1 = filtered.stream()
                .map(e -> e.getAmount1() != null ? e.getAmount1() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal total2 = filtered.stream()
                .map(e -> e.getAmount2() != null ? e.getAmount2() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new WorkReportDto(total1, total2, filtered.size());
    }

    // ---------------- TOTAL AMOUNT 1 ----------------
    public BigDecimal getTotalAmount1(User user, LocalDate from, LocalDate to) {

        return getFilteredEntries(user, from, to).stream()
                .map(e -> e.getAmount1() != null ? e.getAmount1() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ---------------- TOTAL AMOUNT 2 ----------------
    public BigDecimal getTotalAmount2(User user, LocalDate from, LocalDate to) {

        return getFilteredEntries(user, from, to).stream()
                .map(e -> e.getAmount2() != null ? e.getAmount2() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ---------------- CENTRAL FILTER LOGIC ----------------
    private List<WorkEntry> getFilteredEntries(User user, LocalDate from, LocalDate to) {

        if (from == null && to == null) {
            return repository.findByUser(user);
        }

        if (from != null && to == null) {
            return repository.findByUserAndDateGreaterThanEqual(user, from);
        }

        if (from == null && to != null) {
            return repository.findByUserAndDateLessThanEqual(user, to);
        }

        return repository.findByUserAndDateBetween(user, from, to);
    }
}
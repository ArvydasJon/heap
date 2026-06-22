/*package com.arvydas.heap.controller;

import com.arvydas.heap.dto.WorkReportDto;
import com.arvydas.heap.services.WorkReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports/work-entries")
@RequiredArgsConstructor
public class WorkReportController {

    private final WorkReportService service;

    @GetMapping
    public WorkReportDto getReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.getReport(from, to);
    }

    @GetMapping("/total1")
    public BigDecimal totalAmount1(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.getTotalAmount1(from, to);
    }

    @GetMapping("/total2")
    public BigDecimal totalAmount2(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.getTotalAmount2(from, to);
    }
}

 */
package com.arvydas.heap.controller;

import com.arvydas.heap.dto.WorkReportDto;
import com.arvydas.heap.exception.ResourceNotFoundException;
import com.arvydas.heap.model.User;
import com.arvydas.heap.repository.UserRepository;
import com.arvydas.heap.services.WorkReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports/work-entries")
@RequiredArgsConstructor
public class WorkReportController {

    private final WorkReportService service;
    private final UserRepository userRepository;

    // ---------------- FULL REPORT ----------------
    @GetMapping
    public WorkReportDto getReport(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        User user = getCurrentUser();

        return service.getReport(user, from, to);
    }

    // ---------------- TOTAL AMOUNT 1 ----------------
    @GetMapping("/total1")
    public BigDecimal totalAmount1(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        User user = getCurrentUser();

        return service.getTotalAmount1(user, from, to);
    }

    // ---------------- TOTAL AMOUNT 2 ----------------
    @GetMapping("/total2")
    public BigDecimal totalAmount2(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        User user = getCurrentUser();

        return service.getTotalAmount2(user, from, to);
    }

    // ---------------- HELPER ----------------
    private User getCurrentUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
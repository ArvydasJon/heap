package com.arvydas.heap.controller;

import com.arvydas.heap.dto.PageResponse;
import com.arvydas.heap.dto.WorkEntryDto;
import com.arvydas.heap.exception.ResourceNotFoundException;
import com.arvydas.heap.model.User;
import com.arvydas.heap.repository.UserRepository;
import com.arvydas.heap.services.WorkEntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workentry")
@RequiredArgsConstructor
public class WorkEntryController {

    private final WorkEntryService service;
    private final UserRepository userRepository;

    @GetMapping
    public PageResponse<WorkEntryDto> getAll(
            @PageableDefault(size = 10, sort = "date") Pageable pageable,
            @RequestParam(required = false) String userEmail,
            Authentication authentication
    ) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        Page<WorkEntryDto> page;

        if (isAdmin && userEmail != null && !userEmail.isBlank()) {
            User selectedUser = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            page = service.findAllByUser(pageable, selectedUser);
        } else if (isAdmin) {
            page = service.findAll(pageable);
        } else {
            page = service.findAllByUser(pageable, user);
        }

        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @PreAuthorize("hasRole('ADMIN') or @workEntrySecurity.canModify(#id)")
    @GetMapping("/{id}")
    public WorkEntryDto getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public WorkEntryDto create(@Valid @RequestBody WorkEntryDto dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return service.save(dto, user);
    }

    @PreAuthorize("hasRole('ADMIN') or @workEntrySecurity.canModify(#id)")
    @PutMapping("/{id}")
    public WorkEntryDto update(@PathVariable Long id,
                               @Valid @RequestBody WorkEntryDto dto) {
        return service.update(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN') or @workEntrySecurity.canModify(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

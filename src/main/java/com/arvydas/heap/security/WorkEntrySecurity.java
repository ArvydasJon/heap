package com.arvydas.heap.security;

import com.arvydas.heap.model.User;
import com.arvydas.heap.model.WorkEntry;
import com.arvydas.heap.repository.HeapRepository;
import com.arvydas.heap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkEntrySecurity {

    private final HeapRepository repository;
    private final UserRepository userRepository;

    public boolean canModify(Long id) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        WorkEntry entry = repository.findById(id)
                .orElseThrow();

        return entry.getUser().getId().equals(user.getId());
    }
}
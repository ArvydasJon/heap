package com.arvydas.heap.services;

import com.arvydas.heap.dto.WorkEntryDto;
import com.arvydas.heap.model.User;
import com.arvydas.heap.model.WorkEntry;
import com.arvydas.heap.repository.HeapRepository;
import com.arvydas.heap.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkEntryService {

    private final HeapRepository repository;

    // ---------------- Pagination + Sorting ----------------
    public Page<WorkEntryDto> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::toDto);
    }

    // ---------------- Pagination pagal User ----------------
    public Page<WorkEntryDto> findAllByUser(Pageable pageable, User user) {
        return repository.findAllByUser(user, pageable)
                .map(this::toDto);
    }

    // ---------------- CRUD ----------------
    public WorkEntryDto findById(Long id) {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkEntry not found with id: " + id)));
    }

    // Save be user (jeigu reikia)
    public WorkEntryDto save(WorkEntryDto dto) {
        WorkEntry entity = toEntity(dto);
        return toDto(repository.save(entity));
    }

    // Save su user (JWT susietas įrašas)
    public WorkEntryDto save(WorkEntryDto dto, User user) {
        WorkEntry entity = toEntity(dto);
        entity.setUser(user);  // susiejame su prisijungusiu vartotoju
        return toDto(repository.save(entity));
    }

    public WorkEntryDto update(Long id, WorkEntryDto dto) {
        WorkEntry existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkEntry not found with id: " + id));

        existing.setDate(dto.getDate());
        existing.setObject(dto.getObject());
        existing.setWork(dto.getWork());
        existing.setAmount1(dto.getAmount1());
        existing.setAmount2(dto.getAmount2());
        existing.setNotes(dto.getNotes());

        return toDto(repository.save(existing));
    }

    public void delete(Long id) {
        WorkEntry existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkEntry not found with id: " + id));

        repository.delete(existing);
    }

    // ---------------- DTO mappers ----------------
    private WorkEntryDto toDto(WorkEntry entity) {
        return WorkEntryDto.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .object(entity.getObject())
                .work(entity.getWork())
                .amount1(entity.getAmount1())
                .amount2(entity.getAmount2())
                .notes(entity.getNotes())
                .build();
    }

    private WorkEntry toEntity(WorkEntryDto dto) {
        return WorkEntry.builder()
                .id(dto.getId())
                .date(dto.getDate())
                .object(dto.getObject())
                .work(dto.getWork())
                .amount1(dto.getAmount1())
                .amount2(dto.getAmount2())
                .notes(dto.getNotes())
                .build();
    }
}
package com.arvydas.heap.repository;

import com.arvydas.heap.model.WorkEntry;
import com.arvydas.heap.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDate;

public interface HeapRepository extends JpaRepository<WorkEntry, Long> {

    // Suranda visus WorkEntry pagal user su puslapiavimu
    Page<WorkEntry> findAllByUser(User user, Pageable pageable);

    List<WorkEntry> findByUser(User user);
    List<WorkEntry> findByUserAndDateGreaterThanEqual(User user, LocalDate from);
    List<WorkEntry> findByUserAndDateLessThanEqual(User user, LocalDate to);
    List<WorkEntry> findByUserAndDateBetween(User user, LocalDate from, LocalDate to);
}
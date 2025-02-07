package com.example.todojpa.repository;

import com.example.todojpa.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Optional<Todo> findByIdAndUseYnTrue(Long id);
    Page<Todo> findAllByUseYnTrueAndUser_NameAndUpdatedAtOrderByUpdatedAtDesc(String user_name, LocalDateTime updatedAt, Pageable pageable);
}

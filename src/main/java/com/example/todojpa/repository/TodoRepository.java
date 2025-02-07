package com.example.todojpa.repository;

import com.example.todojpa.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Optional<Todo> findByIdAndUseYnTrue(Long id);
    Page<Todo> findAllByUseYnTrueAndUser_NameAndUpdatedAtOrderByUpdatedAtDesc(String user_name, LocalDateTime updatedAt, Pageable pageable);

    @Modifying
    @Query("update Todo t " +
            "  set t.useYn = false " +
            "where 1 = 1" +
            "  and t.user.id = :userId " +
            "  and t.useYn = true")
    int softDeleteByUserId(@Param("userId") Long userId);
}

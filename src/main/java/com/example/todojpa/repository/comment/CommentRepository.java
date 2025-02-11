package com.example.todojpa.repository.comment;

import com.example.todojpa.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Modifying
    @Query(" update Comment c" +
            "   set c.useYn = false" +
            " where 1 = 1" +
            "   and c.user.id = :userId" +
            "   and c.useYn = true")
    int softDeleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(" update Comment c" +
            "   set c.useYn = false" +
            " where 1 = 1" +
            "   and c.todo.id = :todoId" +
            "   and c.useYn = true")
    int softDeleteByTodoId(@Param("todoId") Long todoId);

    List<Comment> findAllByTodoIdAndUseYnTrue(Long todoId);
}

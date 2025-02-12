package com.example.todojpa.repository.comment;

import com.example.todojpa.dto.response.comment.CommentDetail;
import com.example.todojpa.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
@Query(" select c " +
        "  from Comment c " +
        "  left join fetch c.user u " + //삭제된 사용자도 가져오도록, toOne 관계에서는 fetch 써도됨
        "  left join Todo t " +
        "         on c.todo = t " + // 삭제된 일정도 가져오도록 left join
        " where c.todo.id = :todoId " +
        " order by c.level asc, c.createdAt desc")//댓글은 생성일 순서로 정렬한다
    List<Comment> findAllByTodoIdOrderByLevelAscCreatedAtDesc(Long todoId);
}

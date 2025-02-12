package com.example.todojpa.dto.response.comment;

import com.example.todojpa.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
public class CommentDetail {
    private final String comment;
    private final Long todoId;
    private final Long userId;
    private final String userName;
    private final Long id;
    private final Long parentId;
    private final LocalDateTime createdAt;
    private final Boolean isDeleted;
    private final List<CommentDetail> children = new ArrayList<>();

    public CommentDetail(String comment, Long todoId, Long userId,String userName, Long id, Long parentId, Boolean isDeleted, LocalDateTime createdAt) {
        this.comment = isDeleted ? "삭제된 댓글" : comment;
        this.todoId = todoId;
        this.userId = userId;
        this.userName = userName ==null ? "삭제된 사용자" : userName;
        this.id = id;
        this.parentId = parentId;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }

    public static CommentDetail from(Comment comment) {
        return new CommentDetail(
                comment.getDeleted() ? "삭제된 댓글" : comment.getComment(),
                comment.getTodo() == null ? null : comment.getTodo().getId(),
                comment.getUser() == null ? null :  comment.getUser().getId(),
                comment.getUser() == null ? "삭제된 사용자"  :comment.getUser().getName(),
                comment.getId(),
                comment.getParent() == null ? null : comment.getParent().getId(),
                comment.getDeleted(),
                comment.getCreatedAt()
        );
    }

    public static List<CommentDetail> convertTree(List<Comment> comments) {
        List<CommentDetail> res =  new ArrayList<>();
        if(comments.size() == 1) {
            res.add(CommentDetail.from(comments.get(0)));
            return res;
        }
        Map<Long, CommentDetail> map = new HashMap<>();
        comments.forEach(comment -> {
            CommentDetail detail = CommentDetail.from(comment);
            map.put(detail.getId(), detail);//각 댓글모두 부로로써 map에 등록
            if(detail.getParentId() != null) {
                map.get(detail.getParentId()).children.add(detail);
            } else {
                res.add(detail);
            }
        });

        return res;
    }
}

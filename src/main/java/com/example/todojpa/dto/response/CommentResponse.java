package com.example.todojpa.dto.response;

import com.example.todojpa.entity.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponse {
    private final String comment;
    private final Long todoId;
    private final Long userId;
    private final Long id;
    private final Long parentId;
    private final Set<CommentResponse> children;
    private final LocalDateTime createdAt;

    public static CommentResponse from(Comment comment) {
       return new CommentResponse(comment.getComment(),
               comment.getTodo().getId(),
               comment.getUser().getId(),
               comment.getId(),
               comment.getParent() == null ? null : comment.getParent().getId(),
               new TreeSet<>(Comparator.comparing(CommentResponse::getCreatedAt)),
               comment.getCreatedAt()
        );
    }

    public static Set<CommentResponse> convertTree(List<Comment> comments) {
        Set<CommentResponse> res = new TreeSet<>(Comparator.comparing(CommentResponse::getCreatedAt));
        Map<Long, CommentResponse> map = new HashMap<>();
        comments.forEach(comment -> {
            CommentResponse cur = CommentResponse.from(comment);
            map.put(comment.getId(), cur);//각 댓글모두 부로로써 map에 등록
            if(cur.getParentId() != null) {
                map.get(cur.getParentId()).children.add(cur);
            } else {
                res.add(cur);
            }
        });

        return res;
    }
}

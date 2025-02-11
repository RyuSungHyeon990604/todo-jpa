package com.example.todojpa.dto.response.comment;

import com.example.todojpa.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@AllArgsConstructor
public class CommentDetail {
    private final String comment;
    private final Long todoId;
    private final Long userId;
    private final Long id;
    private final Long parentId;
    private final LocalDateTime createdAt;
    private final Set<CommentDetail> children;

    public static CommentDetail from(Comment comment) {
        return new CommentDetail(comment.getComment(),
                comment.getTodo().getId(),
                comment.getUser().getId(),
                comment.getId(),
                comment.getParent() == null ? null : comment.getParent().getId(),
                comment.getCreatedAt(),
                new TreeSet<>(Comparator.comparing(CommentDetail::getCreatedAt))
        );
    }

    public static Set<CommentDetail> convertTree(List<Comment> comments) {
        Set<CommentDetail> res = new TreeSet<>(Comparator.comparing(CommentDetail::getCreatedAt));
        Map<Long, CommentDetail> map = new HashMap<>();
        comments.forEach(comment -> {
            CommentDetail cur = CommentDetail.from(comment);
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

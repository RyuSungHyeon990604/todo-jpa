package com.example.todojpa.dto.response.todo;

import com.example.todojpa.dto.response.comment.CommentDetail;
import com.example.todojpa.entity.Todo;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class TodoDetail {
    private final Long id;
    private final String userName;
    private final String title;
    private final String task;
    private final LocalDateTime createdAt;

    private Long commentCount = 0L;
    private List<CommentDetail> comments = new ArrayList<>();

    public TodoDetail(Todo todo, Long commentCount) {
        this.id = todo.getId();
        this.userName = todo.getUser().getName();
        this.title = todo.getTitle();
        this.task = todo.getTask();
        this.commentCount = commentCount;
        this.createdAt = todo.getCreatedAt();
    }

    public void setComments(List<CommentDetail> comments) {
        this.comments = comments;
        this.commentCount = (long) comments.size();
    }


    public static TodoDetail from(Todo todo) {
        return new TodoDetail(todo.getId(), todo.getUser().getName(), todo.getTitle(), todo.getTask(), todo.getUpdatedAt());
    }
}

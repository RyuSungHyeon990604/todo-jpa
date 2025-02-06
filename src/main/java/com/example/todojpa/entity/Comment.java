package com.example.todojpa.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "todo_id")
    Todo todo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Builder
    public Comment(String comment, Todo todo, User user) {
        this.comment = comment;
        this.todo = todo;
        this.user = user;
    }
}

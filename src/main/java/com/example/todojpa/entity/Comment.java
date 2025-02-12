package com.example.todojpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@SQLDelete(sql = "update comment set deleted_at = now() where id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @Setter
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    Todo todo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Column(nullable = false) @Setter
    private int level;

    @Builder
    public Comment(Comment parent, String comment, int level, Todo todo, User user) {
        this.parent = parent;
        this.comment = comment;
        this.level = level;
        this.todo = todo;
        this.user = user;
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }
}

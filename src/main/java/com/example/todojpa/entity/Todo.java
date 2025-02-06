package com.example.todojpa.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Todo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String task;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL)
    List<Comment> comments = new ArrayList<>();

    @Builder
    public Todo(User user, String title, String task) {
        this.user = user;
        this.title = title;
        this.task = task;
    }

    @Override
    public void softDelete(){
        super.softDelete();
        this.comments.forEach(Comment::softDelete);
    }
}

package com.example.todojpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@SQLDelete(sql = "update todo set deleted_at = now() where id = ?")
@SQLRestriction("deleted_at is null ")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //필요할때만 fetch join or 지연 로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String task;


    @Builder
    public Todo(User user, String title, String task) {
        this.user = user;
        this.title = title;
        this.task = task;
    }

    public void update(String title, String task){
        this.title = title;
        this.task = task;
    }
}

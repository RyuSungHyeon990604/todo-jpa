package com.example.todojpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


@Entity
@Getter
@SQLDelete(sql = "update todo set deleted = true where id = ?")
@SQLRestriction("deleted = false")
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

    @Formula("( select count(c.id) " +
            "    from comment c " +
            "   where c.todo_id = id" +
            "     and c.deleted = false )")
    private Long commentCount;


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

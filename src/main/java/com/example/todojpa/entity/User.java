package com.example.todojpa.entity;

import com.example.todojpa.util.PasswordEncoder;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SQLDelete(sql = "update user set deleted = true where id = ?")
@SQLRestriction("deleted = false")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Todo> todos = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void updateUser(String name, String email, String password){
        if(!this.name.equals(name)){
            this.name = name;
        }
        if(!this.email.equals(email)){
            this.email = email;
        }
        if(!PasswordEncoder.matches(password, this.password)){
            this.password = PasswordEncoder.encode(password);
        }
    }
}

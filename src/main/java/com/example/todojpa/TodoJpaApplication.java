package com.example.todojpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TodoJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoJpaApplication.class, args);
    }

}

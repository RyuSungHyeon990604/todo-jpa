package com.example.todojpa.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class DeleteCheckEntity {
    LocalDateTime deletedAt = null;
}

package com.example.todojpa.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class DeleteCheckEntity {
    Boolean deleted = false;
}


        package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    public boolean getName;

    private UUID id;
    private String name;
    private Instant  createdAt;
    private Instant updatedAt;

    // User 생성
    public User(String name) {
        this.id = UUID.randomUUID();
       this.createdAt = Instant.now();
        this.name = name;
    }

    // User 생성
    public User(String username, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.name = username;
    }

    // ID 조회
    public UUID getId() {
        return id;
    }

    // 이름 조회
    public String getName() {
        return name;
    }

    // 생성 시간 조회
    public Instant  getCreatedAt() {
        return createdAt;
    }

    // 수정 시간 조회
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // 이름 수정
    public void update(String name) {
        this.name = name;
        this.createdAt = Instant.now();
    }
}


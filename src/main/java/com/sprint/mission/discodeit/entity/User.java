
        package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    public boolean getName;

    private UUID id;
    private String name;
    private Long createdAt;
    private Long updatedAt;

    // User 생성
    public User(String name) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.name = name;
    }

    // User 생성
    public User(String username, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
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
    public Long getCreatedAt() {
        return createdAt;
    }

    // 수정 시간 조회
    public Long getUpdatedAt() {
        return updatedAt;
    }

    // 이름 수정
    public void update(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }
}


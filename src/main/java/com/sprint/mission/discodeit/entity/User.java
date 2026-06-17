package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {

    //필드
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String name;
    private String email;

    //생성자 초기화
    public User(String name, String email) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.name = name;
        this.email = email;
    }

    //메서드


    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatdAt() {
        return  updatedAt;
    }

    public  String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setUpdatedAt() {
        return;
    }

}

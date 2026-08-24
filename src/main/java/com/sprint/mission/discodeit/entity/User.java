package com.sprint.mission.discodeit.entity;

import java.util.UUID;
import java.io.Serializable;

public class User implements Serializable {

    //필드
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String name;
    private String email;
    private String password;

    //생성자 초기화
    public User(String name, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //메서드
    public UUID getId() { return id; }

    public long getCreatedAt() { return createdAt; }

    public long getUpdatedAt() { return  updatedAt; }

    public  String getName() { return name; }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public void setUpdatedAt() { return; }

    public void setPassword(String password) { this.password = password; }

    public void setEmail(String email) { this.email = email; }

    public void setName(String name) { this.name = name; }
}

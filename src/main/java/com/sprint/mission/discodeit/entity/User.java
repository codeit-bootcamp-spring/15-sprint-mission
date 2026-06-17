package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private String username;
    private String email;
    private final Long createdAt;
    private Long updatedAt;

    public User (String username, String email){
        this.id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Long getCreatedAt() { return createdAt; }
    public Long getUpdatedAt() { return updatedAt; }

    public void update(String username, String email){
        this.username = username;
        this.email = email;
        this.updatedAt = System.currentTimeMillis();
    }
}

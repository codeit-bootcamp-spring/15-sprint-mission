package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class  User implements Serializable {
    private static final long seriaVersionUID = 1L;

    public boolean getName;
    private UUID id;
    private String name;
    private Long createdAt;
    private Long updatedAt;

    public User(String name) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void update(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }
}

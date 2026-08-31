package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Common implements Serializable {
    private UUID id;
    private Long createdAt, updatedAt;


    public Common() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void autoSetCreatedAt() {
        this.createdAt = System.currentTimeMillis();
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void autoSetUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}

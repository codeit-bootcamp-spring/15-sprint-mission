package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Common {

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    public Common() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    protected void updateUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }
}

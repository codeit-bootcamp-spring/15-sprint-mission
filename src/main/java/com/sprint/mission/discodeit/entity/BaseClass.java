package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

abstract public class BaseClass {
    private final UUID id;
    private final Long createdAt;
    protected Long updatedAt;

    public BaseClass(){
        this.id = UUID.randomUUID();
        long now = Instant.now().toEpochMilli();
        this.createdAt=now;
        this.updatedAt=now;
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now().toEpochMilli();
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
}

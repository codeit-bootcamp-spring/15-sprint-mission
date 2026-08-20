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

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }

}

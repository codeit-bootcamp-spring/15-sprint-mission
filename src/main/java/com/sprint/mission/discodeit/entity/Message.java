package com.sprint.mission.discodeit.entity;

import java.util.Map;
import java.util.UUID;

public class Message {

    private UUID id;
    private Long createdAt;
    private Long updatedAt;


    public Message() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatdAt() {
        return updatedAt;
    }



}
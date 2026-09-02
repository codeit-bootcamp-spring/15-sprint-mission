package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;


public class Message implements Serializable {

    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private UUID channelId;
    private UUID userId;
    private String content;


    public Message(UUID channelId, UUID userId, String content) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.channelId = channelId;
        this.userId = userId;
        this.content = content;

    }

    public UUID getChannelId() {
        return channelId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

}
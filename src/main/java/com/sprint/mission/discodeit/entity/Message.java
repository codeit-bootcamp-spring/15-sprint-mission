package com.sprint.mission.discodeit.entity;

import java.util.Map;
import java.util.UUID;


public class Message {

    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private UUID channelId;
    private String content;

    public Message(UUID channelId, String content) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.channelId = channelId;
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

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }



}
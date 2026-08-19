package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    private String content;
    private UUID userID;
    private UUID channelID;


    public Message(String content, UUID userID, UUID channelID) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.content = content;
        this.userID = userID;
        this.channelID = channelID;
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

    public String getContent() {
        return content;
    }

    public UUID getChannelID() {
        return channelID;
    }

    public UUID getUserID() {
        return userID;
    }

    public void update(String content) {
        this.content = content;
    }
}

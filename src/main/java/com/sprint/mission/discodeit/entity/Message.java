package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String contents;
    private final UUID userId;
    private final UUID channelId;

    public Message(String content, UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.contents = contents;
        this.userId = userId;
        this.channelId = channelId;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUqdatedAt() {
        return updatedAt;
    }

    public String getContents() {
        return contents;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return  channelId;
    }

    public void update(String contents) {
        this.contents = contents;
        this.updatedAt = System.currentTimeMillis();
    }

}

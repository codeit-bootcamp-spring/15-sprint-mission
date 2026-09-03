package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private String content;
    private UUID channelId;
    private UUID authorId;
    private final Long createdAt;
    private Long updatedAt;

    public  Message(String content, UUID channelId, UUID authorId){
        this.id = UUID.randomUUID();
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() { return id; }
    public String getContent() { return content; }
    public UUID getChannelId() { return channelId; }
    public UUID getAuthorId() { return authorId; }
    public Long getCreatedAt() { return createdAt; }
    public Long getUpdatedAt() { return updatedAt; }

    public void update(String content){
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }
}

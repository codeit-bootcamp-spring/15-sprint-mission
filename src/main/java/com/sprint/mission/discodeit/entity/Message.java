package com.sprint.mission.discodeit.entity;
import java.util.UUID;


public class Message {
    private final UUID messageId;
    private final UUID userId;
    private final UUID channelId;
    private final long createdAt;
    private long updatedAt;
    private String content;
    public Message(String content, UUID channelId,UUID userId){
        this.userId = userId;
        this.messageId = UUID.randomUUID();
        this.content = content;
        this.channelId = channelId;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }
    public UUID getMessageId() {return messageId;}
    public long getUpdatedAt(){
        return updatedAt;
    }
    public long getCreatedAt() {
        return createdAt;
    }
    public String getContent() {
        return content;
    }
    public UUID getUserId() {
        return userId;
    }
    public UUID getChannelId() {
        return channelId;
    }
    public void update(String content){
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }
}

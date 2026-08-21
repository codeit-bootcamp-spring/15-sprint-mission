package com.sprint.mission.discodeit.entity;
import java.util.UUID;


public class Message {
    private final UUID channelId;
    private final UUID UserId;
    private final UUID messageId;
    private final long createdAt;
    private long updatedAt;
    private String message;
    public Message(String contents, UUID channelId,UUID userId){
        this.channelId = channelId;
        this.UserId = userId;
        this.messageId = UUID.randomUUID();
        this.message = contents;
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
    public void update(String message){
        this.message = message;
        this.updatedAt = System.currentTimeMillis();
    }
}

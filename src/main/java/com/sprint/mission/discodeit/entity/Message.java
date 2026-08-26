package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {

    private UUID id;
    private UUID userid;
    private UUID ChannelId;
    private String content;
    private Long createdAt;
    private Long updateAt;

    public Message(UUID userid , UUID channelId, String content) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.userid = userid;
        this.ChannelId = channelId;
        this.content = content;
    }

    public Message(String 안녕하세요_봄의_요정_길춘배) {
    }

    public Message(UUID id, UUID id1) {
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserid() {
        return userid;
    }

    public UUID getChannelId() {
        return ChannelId;
    }

    public String getContent() {
        return content;
    }

    public Long getCreatedAt() {
        return createdAt;
    }


    public Long getUpdateAt() {
        return updateAt;
    }

    public void update(String content) {
        this.content = content;
        this.updateAt =System.currentTimeMillis();
    }

}

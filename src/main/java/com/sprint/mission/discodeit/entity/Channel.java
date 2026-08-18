package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {

    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String channelName;


    public Channel(String channelName) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.channelName = channelName;
    }

    public void update(String channelName) {
        this.channelName = channelName;
        this.updatedAt = System.currentTimeMillis();
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
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

    public String getChannelName() {
        return channelName;
    }
}

package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Channel implements Serializable {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String channelName;
    private String topic;

    public Channel(String channelName, String topic) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.channelName = channelName;
        this.topic = topic;
    }

    public UUID getId() {
        return id;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getTopic() {
        return topic;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void update(String channelName, String topic) {
        this.channelName = channelName;
        this.topic = topic;
    }
}

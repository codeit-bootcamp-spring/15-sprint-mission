package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Channel implements Serializable {
    private static final long seriaVersionUID = 1L;

    private UUID id;
    private String name;
    private ChannelType type;
    private Long createdAt;
    private Long updatedAt;

    public Channel(String name,ChannelType type) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.name = name;
        this.type = ChannelType.PUBLIC;
    }

    public Channel(ChannelType type, String name, String description) {
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ChannelType getType() {
        return type;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void update(String name, ChannelType type) {
        this.name = name;
        this.type = type;
        this.updatedAt = System.currentTimeMillis();
    }
}

package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class Channel implements Serializable {
    private static final long seriaVersionUID = 1L;

    private UUID id;
    private String name;
    private ChannelType type;
    private Instant  createdAt;
    private Instant updatedAt;


    public Channel(ChannelType type, String name, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.name = name;
        this.type = ChannelType.PUBLIC;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void update(String name, ChannelType type) {
        this.name = name;
        this.type = type;
        this.updatedAt = Instant.now();
    }
}

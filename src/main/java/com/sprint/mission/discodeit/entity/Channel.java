package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private String name;
    private final ChannelType type;

    private final Instant createdAt;
    private Instant updatedAt;

    public Channel(String name, ChannelType type){
        this.id = UUID.randomUUID();
        this.name = name;
        this.type = type;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void  update(String name){
        this.name = name;
        this.updatedAt = Instant.now();
    }
}

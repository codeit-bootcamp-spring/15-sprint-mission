package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private String name;
    private final Long createdAt;
    private Long updatedAt;

    public Channel(String name){
        this.id = UUID.randomUUID();
        this.name = name;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public Long getCreatedAt() { return createdAt; }
    public Long getUpdatedAt() { return updatedAt; }

    public void  update(String name){
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }
}

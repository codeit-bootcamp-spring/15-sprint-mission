package com.sprint.mission.discodeit.entity;
import java.util.UUID;

public class User {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private String username;
    public User(String username){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.username = username;
    }
    public UUID getId(){
        return id;
    }
    public long getCreatedAt(){
        return createdAt;
    }
    public long getUpdatedAt(){
        return updatedAt;
    }
    public void update(String username) {
        this.username = username;
        this.updatedAt = System.currentTimeMillis();
    }

}

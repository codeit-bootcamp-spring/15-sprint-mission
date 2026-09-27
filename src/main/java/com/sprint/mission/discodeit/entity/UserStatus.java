package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseClass {
    private final UUID userId;
    private Instant lastActiveAt;

    public UserStatus(UUID userId){
        super();
        this.userId=userId;
        this.lastActiveAt=this.updatedAt;
    }

    public void update(){

        setUpdatedAt();
        this.lastActiveAt=updatedAt;
    }

    public boolean isOnline(){
        return Duration.between(getUpdatedAt(), Instant.now()).toMinutes() < 5;
    }

}

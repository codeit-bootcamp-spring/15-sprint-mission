package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseClass {
    private final UUID userId;

    public UserStatus(UUID userId){
        super();
        this.userId=userId;
    }

    public void update(){
        setUpdatedAt();
    }

    public boolean isOnline(){
        return Duration.between(getUpdatedAt(), Instant.now()).toMinutes() < 5;
    }

}

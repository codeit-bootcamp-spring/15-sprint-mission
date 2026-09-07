package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends Common{

    // 마지막 활동(접속)시간
    private Instant updateAt;

    public UserStatus(UUID id) {
        super(id);
    }

    public boolean isOnline(){
        Instant now = Instant.now();
        Duration diff = Duration.between(now , updateAt);
        return diff.toMinutes() < 5;
    }

}

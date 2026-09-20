package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatusResponse {
    private final UUID id;
    private final UUID userId;
    private final Instant lastActiveAt;
    private final Boolean isOnline;

    private UserStatusResponse(UUID id, UUID userId, Instant lastActiveAt, boolean isOnline){
        this.id = id;
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
        this.isOnline = isOnline;
    }

    public static UserStatusResponse from(UserStatus userStatus){
        return new UserStatusResponse(userStatus.getId(), userStatus.getUserId(), userStatus.getLastActiveAt(), userStatus.isOnline());
    }
}

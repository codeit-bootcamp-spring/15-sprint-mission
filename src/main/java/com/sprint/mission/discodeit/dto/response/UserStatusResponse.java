package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserStatusResponse {
    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    //
    private final UUID userId;
    private final Instant lastActiveAt;

    public static UserStatusResponse from(UserStatus userStatus) {
        return new UserStatusResponse(userStatus.getId(), userStatus.getCreatedAt(), userStatus.getUpdatedAt(),
                userStatus.getUserId(), userStatus.getLastActiveAt());
    }
}

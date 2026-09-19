package com.sprint.mission.discodeit.dto.response;

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
}

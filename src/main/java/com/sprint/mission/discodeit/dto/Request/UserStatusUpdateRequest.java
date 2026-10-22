package com.sprint.mission.discodeit.dto.Request;

import java.time.Instant;

public record UserStatusUpdateRequest(
        Instant newLastActiveAt
) {

}

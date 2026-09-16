package com.sprint.mission.discodeit.dto.UserStatusDto;

import java.time.Instant;

public record UserStatusUpdateRequest(

        Instant lastActiveAt
) {
}

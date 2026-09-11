package com.sprint.mission.discodeit.dto.Request;

import java.util.UUID;

public record MessageUpdateRequest(
        UUID id, String messageString
) {
}

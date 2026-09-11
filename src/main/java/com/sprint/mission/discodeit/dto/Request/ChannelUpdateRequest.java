package com.sprint.mission.discodeit.dto.Request;

import java.util.UUID;

public record ChannelUpdateRequest(
        UUID id ,
        String name
) {
}

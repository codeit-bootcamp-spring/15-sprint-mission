package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequest(
        String contents,
        UUID channelId,
        UUID authorId,
        List<UUID> attachmentIds
) {
}

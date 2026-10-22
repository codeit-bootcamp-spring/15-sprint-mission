package com.sprint.mission.discodeit.dto.Response;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant lastReadAt
) {
    public static ReadStatusDto from(ReadStatus readStatus) {
        return new ReadStatusDto(readStatus.getId(), readStatus.getUser().getId(),
                readStatus.getChannel().getId(), readStatus.getLastReadAt());
    }
}

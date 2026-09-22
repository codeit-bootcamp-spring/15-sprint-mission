package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ReadStatusResponse {
    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    //
    private final UUID userId;
    private final UUID channelId;
    private final Instant lastReadAt;

    public static ReadStatusResponse from(ReadStatus status){
        return new ReadStatusResponse(status.getId(), status.getCreatedAt(), status.getUpdatedAt(),
                status.getUserId(), status.getChannelId(), status.getLastReadAt());
    }
}

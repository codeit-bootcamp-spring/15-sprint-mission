package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends Common {
    private final UUID userId; // 채널별로 1:N 관계.eeeeeeeeeeee
    private final UUID channelId; // 사용자별로 1:N 관계 => 사용자 + 채널은 1대1 관계
    @Setter
    private Instant lastReadAt;


    public ReadStatus(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = null;
    }
}

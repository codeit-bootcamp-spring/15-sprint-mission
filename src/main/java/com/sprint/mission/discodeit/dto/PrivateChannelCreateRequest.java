package com.sprint.mission.discodeit.dto;
import lombok.Getter;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Getter
public class PrivateChannelCreateRequest {
    private final List<UUID> userIds;
    public PrivateChannelCreateRequest(List<UUID> userIds) {
        this.userIds = List.copyOf(userIds);
    }
}

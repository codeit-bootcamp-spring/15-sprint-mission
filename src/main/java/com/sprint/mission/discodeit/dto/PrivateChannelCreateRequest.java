package com.sprint.mission.discodeit.dto;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(List<UUID> userIds) {
    public PrivateChannelCreateRequest(List<UUID> userIds) {
        this.userIds = List.copyOf(userIds);
    }
}

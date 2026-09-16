package com.sprint.mission.discodeit.dto;
import lombok.Getter;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Getter
public class ChannelUpdateRequest {
    private final UUID id;
    private final String name;
    private final String description;
    public ChannelUpdateRequest(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}

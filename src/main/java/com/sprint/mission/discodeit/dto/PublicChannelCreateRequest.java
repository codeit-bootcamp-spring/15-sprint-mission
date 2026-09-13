package com.sprint.mission.discodeit.dto;
import lombok.Getter;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Getter
public class PublicChannelCreateRequest {
    private final String name;
    private final String description;
    public PublicChannelCreateRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

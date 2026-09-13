package com.sprint.mission.discodeit.dto;
import lombok.Getter;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Getter
public class MessageUpdateRequest {
    private final UUID id;
    private final String content;
    public MessageUpdateRequest(UUID id, String content) {
        this.id = id;
        this.content = content;
    }
}

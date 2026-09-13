package com.sprint.mission.discodeit.dto;
import lombok.Getter;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Getter
public class MessageCreateRequest {
    private final UUID channelId;
    private final UUID authorId;
    private final String content;
    public MessageCreateRequest(UUID channelId, UUID authorId, String content) {
        this.channelId = channelId;
        this.authorId = authorId;
        this.content = content;
    }
}

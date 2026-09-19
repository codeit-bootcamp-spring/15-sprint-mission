package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {
    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    //
    private final String content;
    //
    private final UUID channelId;
    private final UUID authorId;
    private final List<UUID> attachmentIds;

    public static MessageResponse from(Message message) {
        return new MessageResponse(message.getId(), message.getCreatedAt(), message.getCreatedAt(),
                message.getContent(), message.getChannelId(), message.getAuthorId(), message.getAttachmentIds());
    }
}

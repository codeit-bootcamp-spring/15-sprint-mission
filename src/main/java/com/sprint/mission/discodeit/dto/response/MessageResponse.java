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
@NoArgsConstructor
public class MessageResponse {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    //
    private String content;
    //
    private UUID channelId;
    private UUID authorId;
    private List<UUID> attachmentIds;

    public static MessageResponse from(Message message) {
        return new MessageResponse(message.getId(), message.getCreatedAt(), message.getCreatedAt(),
                message.getContent(), message.getChannelId(), message.getAuthorId(), message.getAttachmentIds());
    }
}

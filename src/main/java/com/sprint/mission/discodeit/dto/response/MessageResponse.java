package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class MessageResponse {
    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String content;
    private final UUID channelId;
    private final UUID authorId;
    private final List<UUID> attachmentIds;


    public MessageResponse(UUID id, Instant createdAt, Instant updatedAt, String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
        this.attachmentIds = attachmentIds;
    }

    public static MessageResponse from(Message message){
        return new MessageResponse(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                message.getChannelId(),
                message.getAuthorId(),
                message.getAttachmentIds()
        );
    }
}

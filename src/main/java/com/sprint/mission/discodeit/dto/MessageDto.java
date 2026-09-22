package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public class MessageDto {

    private UUID id;
    private UUID channelId;
    private UUID authorId;
    private String content;

    public MessageDto(
            UUID id,
            UUID channelId,
            UUID authorId,
            String content
    ) {
        this.id = id;
        this.channelId = channelId;
        this.authorId = authorId;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }
}

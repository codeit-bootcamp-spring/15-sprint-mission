package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private String content;
    private UUID channelId;
    private UUID authorId;

    private List<UUID> attachmentIds;

    private final Instant createdAt;
    private Instant updatedAt;

    public Message(
            String content,
            UUID channelId,
            UUID authorId,
            List<UUID> attachmentIds
    ) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
        this.attachmentIds = attachmentIds;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = Instant.now();
    }
}

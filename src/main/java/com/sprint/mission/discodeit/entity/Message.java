package com.sprint.mission.discodeit.entity;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;
@Getter
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private final UUID channelId;
    private final UUID authorId;
    private String content;
    private final List<UUID> attachmentIds;
    private Instant updatedAt = getCreatedAt();

    public Message(UUID channelId, UUID authorId, String content, List<UUID> attachmentIds) {
        this.channelId = channelId;
        this.authorId = authorId;
        this.content = content;
        this.attachmentIds = List.copyOf(attachmentIds);
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public void update(String content) {
        this.content = content;
        updatedAt = Instant.now();
    }
}

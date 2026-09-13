package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID channelId;
    private UUID authorId;
    private String content;
    private List<UUID> attachmentIds;


    public Message(UUID channelId, UUID authorId, String content, List<UUID> attachmentIds) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.channelId = channelId;
        this.authorId = authorId;
        this.content = content;

        this.attachmentIds =
                attachmentIds == null
                        ? List.of()
                        : List.copyOf(attachmentIds);
    }

    public void setContent(String content) {
        this.content = content;
        this.updatedAt = Instant.now();
    }
}
package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends Common {
    private String contents;
    private final UUID userId;
    private final UUID channelId;
    private final List<UUID> attachmentIds;

    public Message(String contents, UUID userId, UUID channelId, List<UUID> attachmentIds) {
        this.contents = contents;
        this.userId = userId;
        this.channelId = channelId;
        this.attachmentIds = attachmentIds;
    }

    public void update(String contents) {
        this.contents = contents;
        updateUpdatedAt();
    }

}

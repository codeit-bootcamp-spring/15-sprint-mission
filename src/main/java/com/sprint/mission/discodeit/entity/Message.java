package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends Common {
    private String contents;
    private final UUID userId;
    private final UUID channelId;

    public Message(String contents, UUID userId, UUID channelId) {
        this.contents = contents;
        this.userId = userId;
        this.channelId = channelId;
    }

    public String getContents() {
        return contents;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return  channelId;
    }

    public void update(String contents) {
        this.contents = contents;
        updateUpdatedAt();
    }

}

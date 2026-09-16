package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Message extends Common {
    private String content;
    private final UUID channelId;
    private final UUID authorId;
    private final List<UUID> attachmentIds; //binaryContent의 id

    public Message(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
        super();
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
        this.attachmentIds = attachmentIds;
    }


    public void setMessage(String content) {
        this.content = content;
    }
}

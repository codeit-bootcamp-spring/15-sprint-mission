package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Message extends Common implements Serializable {

    private static final long serialVersionUID = 1L;

    private Instant updateAt;

    private String contents;
    private final UUID channelId;
    private final UUID authorId;

    public Message(UUID channelId, UUID authorId, String contents){
        super(UUID.randomUUID());
        this.updateAt = getCreateAt();
        this.authorId = authorId;
        this.channelId = channelId;
        this.contents = contents;
    }

    public void update(String contents){
        this.contents = contents;
        this.updateAt = Instant.now();
    }

}

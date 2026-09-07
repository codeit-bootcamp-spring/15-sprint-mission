package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Long createAt;
    private Long updateAt;

    private String contents;
    private final UUID channelId;
    private final UUID authorId;

    public Message(UUID channelId, UUID authorId, String contents){
        this.id = UUID.randomUUID();
        this.createAt = System.currentTimeMillis();
        this.updateAt = this.createAt;
        this.authorId = authorId;
        this.channelId = channelId;
        this.contents = contents;
    }

    public void update(String contents){
        this.contents = contents;
        this.updateAt = System.currentTimeMillis();
    }

}

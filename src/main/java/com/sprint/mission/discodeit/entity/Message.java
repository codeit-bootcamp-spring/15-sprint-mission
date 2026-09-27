package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.Instant;
import java.util.*;

//누가,머라고,반응,
@Getter
public class Message extends BaseClass  {
    private final UUID channelId;
    private final UUID authorId;
    private String content;
    private List<UUID> attachmentIds;


    //////////////////////////////////

    @JsonCreator
    public Message(
            @JsonProperty("id") UUID id,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("updatedAt") Instant updatedAt,
            @JsonProperty("channelId") UUID channelId,
            @JsonProperty("authorId") UUID userId,
            @JsonProperty("content") String content,
            @JsonProperty("attachmentIds") List<UUID> attachmentIds
    ) {
        super(id, createdAt, updatedAt);
        this.channelId = channelId;
        this.authorId = userId;
        this.content = content;
        this.attachmentIds = attachmentIds == null
                ? new ArrayList<>()
                : new ArrayList<>(attachmentIds);
    }

    public Message(UUID channelId, UUID userId , String content, List<UUID> attachmentIds){
        this.channelId=channelId;
        this.authorId =userId;
        this.content = content;
        this.attachmentIds = attachmentIds == null
                ? new ArrayList<>()
                : new ArrayList<>(attachmentIds);
    }





    public void update(String message){
        this.content =message;
        //this.attachmentIds=attachmentIds;
        setUpdatedAt();
    }


}

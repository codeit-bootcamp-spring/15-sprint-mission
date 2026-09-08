package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

//누가,머라고,반응,
@Getter
public class Message extends BaseClass  {
    private final UUID channelId;
    private final UUID userId;
    private String message;


    //////////////////////////////////

    @JsonCreator
    public Message(
            @JsonProperty("id") UUID id,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("updatedAt") Instant updatedAt,
            @JsonProperty("channelId") UUID channelId,
            @JsonProperty("userId") UUID userId,
            @JsonProperty("message") String message
    ) {
        super(id, createdAt, updatedAt);
        this.channelId = channelId;
        this.userId = userId;
        this.message = message;
    }

    public Message(UUID channelId, UUID userId , String message){
        this.channelId=channelId;
        this.userId=userId;
        this.message=message;
    }





    public void update(String message){
        this.message=message;
        setUpdatedAt();
    }


}

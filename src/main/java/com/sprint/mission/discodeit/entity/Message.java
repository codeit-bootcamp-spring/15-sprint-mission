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
    private List<UUID> binaryIds;


    //////////////////////////////////

    @JsonCreator
    public Message(
            @JsonProperty("id") UUID id,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("updatedAt") Instant updatedAt,
            @JsonProperty("channelId") UUID channelId,
            @JsonProperty("userId") UUID userId,
            @JsonProperty("message") String message,
            @JsonProperty("binaryIds") List<UUID> binaryIds
    ) {
        super(id, createdAt, updatedAt);
        this.channelId = channelId;
        this.userId = userId;
        this.message = message;
        this.binaryIds=binaryIds;
    }

    public Message(UUID channelId, UUID userId , String message, List<UUID> binaryIds){
        this.channelId=channelId;
        this.userId=userId;
        this.message=message;
        this.binaryIds=binaryIds;
    }





    public void update(String message){
        this.message=message;
        //this.binaryIds=binaryIds;
        setUpdatedAt();
    }


}

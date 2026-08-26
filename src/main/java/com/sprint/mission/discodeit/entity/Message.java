package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

//누가,머라고,반응,
public class Message extends BaseClass  {
    private final UUID userId;
    private String message;


    //////////////////////////////////

    @JsonCreator
    public Message(
            @JsonProperty("id") UUID id,
            @JsonProperty("createdAt") Long createdAt,
            @JsonProperty("updatedAt") Long updatedAt,
            @JsonProperty("userId") UUID userId,
            @JsonProperty("message") String message
    ) {
        super(id, createdAt, updatedAt);

        this.userId = userId;
        this.message = message;
    }

    public Message(UUID userId , String message){
        this.userId=userId;
        this.message=message;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }



    public void update(String message){
        this.message=message;
        setUpdatedAt();
    }


}

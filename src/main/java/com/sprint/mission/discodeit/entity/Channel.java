package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Getter
public class Channel extends BaseClass {
    ///////////////////////////////////////////

    private String name;
    private ChannelType channelType;
    ///////////////////////////////////////////

    @JsonCreator
    public Channel(
            @JsonProperty("id") UUID id,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("updatedAt") Instant updatedAt,
            @JsonProperty("name") String name,
            @JsonProperty("channelType") ChannelType channelType
    ) {
        super(id, createdAt, updatedAt);
        this.name = name;
    }

    public Channel(String name , ChannelType channelType) {
        super();
        this.name = name;
        this.channelType=channelType;
    }

    public void update(String name, ChannelType channelType){
        this.name = name;
        this.channelType = channelType;
        setUpdatedAt();
    }








}

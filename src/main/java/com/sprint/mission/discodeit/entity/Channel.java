package com.sprint.mission.discodeit.entity;

import lombok.Getter;


@Getter
public class Channel extends BaseClass {
    ///////////////////////////////////////////

    private String name;
    private String description;
    private final ChannelType channelType;
    ///////////////////////////////////////////



    public Channel(String name ,String description, ChannelType channelType) {
        super();
        this.name = name;
        this.description = description;
        this.channelType=channelType;
    }

    public void update(String name, String description){
        this.name = name;
        this.description = description;
        //this.channelType = channelType;
        setUpdatedAt();
    }








}

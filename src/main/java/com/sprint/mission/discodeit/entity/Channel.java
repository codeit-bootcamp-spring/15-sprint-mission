package com.sprint.mission.discodeit.entity;

import lombok.Getter;

@Getter
public class Channel extends Common {
    private final ChannelType channelType;
    private String channelName;
    private String description;

    // PUBLIC 채널
    public Channel(ChannelType channelType, String channelName, String description) {
        this.channelType = channelType;
        this.channelName = channelName;
        this.description = description;
    }

    // PRIVATE 채널
    public Channel(ChannelType channelType) {
        this.channelType = channelType;
        this.channelName = null;
        this.description = null;
    }

    public void update(String channelName, String description) {
        if (channelName != null) {
            this.channelName = channelName;
        }
        if (description != null) {
            this.description = description;
        }
        updateUpdatedAt();
    }
}
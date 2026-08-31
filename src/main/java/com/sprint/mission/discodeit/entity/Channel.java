package com.sprint.mission.discodeit.entity;

public class Channel extends Common {
    ChannelType channelType;
    String channelName;
    String channelDescription;


    public Channel(ChannelType channelType, String channelName, String channelDescription) {
        super();
        this.channelType = channelType;
        this.channelName = channelName;
        this.channelDescription = channelDescription;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelDescription() {
        return channelDescription;
    }

    public void setChannelDescription(String channelDescription) {
        this.channelDescription = channelDescription;
    }
}

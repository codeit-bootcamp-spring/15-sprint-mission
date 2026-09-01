package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}

package com.sprint.mission.discodeit.entity;

import lombok.Getter;

@Getter
public class Channel extends Common {
    private String channelName;

    public Channel(String channelName) {
        this.channelName = channelName;
    }

    public void update(String channelName) {
        this.channelName = channelName;
        updateUpdatedAt();
    }
}

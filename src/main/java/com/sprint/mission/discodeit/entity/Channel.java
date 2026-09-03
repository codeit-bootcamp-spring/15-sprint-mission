package com.sprint.mission.discodeit.entity;

public class Channel extends Common {
    private String channelName;

    public Channel(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void update(String channelName) {
        this.channelName = channelName;
        updateUpdatedAt();
    }
}

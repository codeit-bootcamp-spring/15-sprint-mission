package com.sprint.mission.discodeit.entity;
import java.util.UUID;

public class Channel {
    private final UUID channelId;
    private final long createAt;
    private long updatedAt;
    private String channelName;
    public Channel(String channelName){
        this.channelId = UUID.randomUUID();
        this.channelName = channelName;
        this.createAt = System.currentTimeMillis();
        this.updatedAt = createAt;
    }

    public UUID getChannelId() {
        return channelId;
    }
    public long getCreateAt() {
        return createAt;
    }
    public long getUpdatedAt() {
        return updatedAt;
    }
    public void update(String channelName){
        this.channelName = channelName;
        this.updatedAt = System.currentTimeMillis();
    }
}

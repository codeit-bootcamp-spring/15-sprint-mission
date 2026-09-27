package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
@Getter
public class ReadStatus extends BaseClass{
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt=this.updatedAt;
    }

    public void update(){

        setUpdatedAt();
        this.lastReadAt=updatedAt;
    }


}

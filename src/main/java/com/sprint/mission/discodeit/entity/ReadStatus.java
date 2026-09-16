package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;
@Getter
public class ReadStatus extends BaseClass{
    private final UUID userId;
    private final UUID channelId;

    public ReadStatus(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;

    }

    public void update(){
        setUpdatedAt();
    }


}

package com.sprint.mission.discodeit.entity;


import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;


@Getter
@Setter
public class ReadStatus extends BaseEntity{

    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt)
    {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = Instant.now();


    }

    public void updateLastReadAt(Instant lastReadAt)
    {
        this.lastReadAt = Instant.now();
    }




}

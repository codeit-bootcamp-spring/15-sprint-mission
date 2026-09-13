package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Channel implements Serializable {

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String channelName;
    private String channelDescription;
    private ChannelType type;
    private UUID ownerId;


    public Channel(
            UUID ownerId,
            String channelName,
            String channelDescription,
            ChannelType type
    ) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.ownerId = ownerId;
        this.channelName = channelName;
        this.channelDescription = channelDescription;
        this.type = type;
    }

    public void update(
            String channelName,
            String channelDescription
    ) {

        if (channelName != null) {
            this.channelName = channelName;
        }

        if (channelDescription != null) {
            this.channelDescription = channelDescription;
        }

        this.updatedAt = Instant.now();
    }
}
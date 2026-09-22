package com.sprint.mission.discodeit.dto;


import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public class ChannelUpdateRequest {

    private UUID id;
    private  String name;
    private ChannelType type;

    public ChannelUpdateRequest(UUID id, String name, ChannelType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public ChannelType getType() {
        return type;
    }
}

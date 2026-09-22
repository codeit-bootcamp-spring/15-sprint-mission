package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;

public class ChannelCreateRequest {

    private String name;
    private ChannelType type;
    private String description;

    public ChannelCreateRequest(String name, ChannelType type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public ChannelType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}

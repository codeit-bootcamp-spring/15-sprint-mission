package com.sprint.mission.discodeit.DTO;

import java.util.UUID;

public class ChannelDto {


    private UUID id;
    private String name;

    public ChannelDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;

    }

}

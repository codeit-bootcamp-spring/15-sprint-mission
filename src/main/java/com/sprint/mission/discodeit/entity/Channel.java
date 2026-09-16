package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Channel extends Common {
    ChannelType type;
    String name;
    String description;


    public Channel(ChannelType type, String name, String description) {
        super();
        this.type = type;
        this.name = name;
        this.description = description;
    }
}

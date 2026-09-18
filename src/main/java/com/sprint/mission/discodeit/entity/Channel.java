package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class Channel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private final ChannelType type;

    public Channel(String name, String description) {
        this(name, description, ChannelType.PUBLIC);
    }
//todo 이거 해야할꺼같다.
    public Channel(String name, String description,ChannelType type)
    {
        super();
        this.name = name;
        this.description = description;
        this.type = ( type != null )? type : ChannelType.PUBLIC;
    }
//todo 확인용
    public void update(String name, String description)
    {
        this.name = name;
        this.description = description;
        touch();
    }

}



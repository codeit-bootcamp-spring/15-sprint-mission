package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "channels")
@Getter
public class Channel extends BaseUpdatableEntity {
    @Column(length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ChannelType type;

    protected Channel(){}


    public Channel(String name ,String description, ChannelType channelType) {
        super();
        this.name = name;
        this.description = description;
        this.type =channelType;
    }

    public void update(String name, String description){
        this.name = name;
        this.description = description;
    }

}

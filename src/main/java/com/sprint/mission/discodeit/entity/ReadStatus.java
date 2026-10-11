package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;


@Entity
@Getter
@Table(
        name = "read_statuses",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "channel_id"})
        }
)
public class ReadStatus extends BaseUpdatableEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id",nullable = false)
    private Channel channel;

    @Column(nullable = false)
    private Instant lastReadAt;

    protected ReadStatus(){

    }

    public ReadStatus(User user, Channel channel,Instant lastReadAt) {
        super();
        this.user = user;
        this.channel = channel;
        this.lastReadAt=lastReadAt;
    }

    public void update(Instant lastReadAt){
        this.lastReadAt=lastReadAt;
    }


}

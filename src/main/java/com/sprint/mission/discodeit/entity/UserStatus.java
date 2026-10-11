package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Table(name = "user_statuses")
@Entity
@Getter
public class UserStatus extends BaseUpdatableEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",unique = true,nullable = false)
    private User user;
    
    @Column(nullable = false)
    private Instant lastActiveAt;

    protected UserStatus(){}

    public UserStatus(User user, Instant lastActiveAt){
        super();
        this.user=user;
        this.lastActiveAt=lastActiveAt;
        user.setStatus(this);

    }

    public void update(Instant lastActiveAt){

        this.lastActiveAt=lastActiveAt;
    }

    public boolean isOnline(){
        return Duration.between(getLastActiveAt(), Instant.now()).toMinutes() < 5;
    }

}

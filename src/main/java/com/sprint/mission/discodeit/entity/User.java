package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

@Getter
public class User extends Common implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nickName;
    private Instant updateAt;

    public User(String nickName){
        super(UUID.randomUUID());
        this.nickName = nickName;
        this.updateAt = getCreateAt();
    }

    public void updateUser(String nickName){
        this.nickName = nickName;
        this.updateAt = Instant.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", nickName=" + nickName +
                ", createAt=" + getCreateAt().atZone(ZoneId.of("Asia/Seoul")) +
                ", updateAt=" + updateAt.atZone(ZoneId.of("Asia/Seoul")) +
                '}';
    }


}

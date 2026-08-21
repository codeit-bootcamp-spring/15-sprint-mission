package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private String nickName;
    private final Long createAt;
    private Long updateAt;

    public User(String nickName){
        this.id = UUID.randomUUID();
        this.nickName = nickName;
        this.createAt = System.currentTimeMillis();
        this.updateAt = this.createAt;
    }

    public void updateUser(String nickName){
        this.nickName = nickName;
        this.updateAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickName=" + nickName +
                ", createAt=" + Instant.ofEpochMilli(createAt) +
                ", updateAt=" + Instant.ofEpochMilli(updateAt) +
                '}';
    }
}

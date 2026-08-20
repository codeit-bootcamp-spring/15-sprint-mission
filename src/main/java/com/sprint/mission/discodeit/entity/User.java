package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String name;
    private String phoneNum;

    public User(String name, String phoneNum) {
         this.id = UUID.randomUUID();
         this.createdAt = System.currentTimeMillis();
         this.name = name;
         this.phoneNum = phoneNum;
        // this.createdAt = ??

    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }
    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void update(String name, String phoneNum) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.updatedAt = System.currentTimeMillis();
    }


}

package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
import java.io.Serializable;

@Getter
public class User implements Serializable {

    //필드
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String name;
    private String email;
    private String password;
    private UUID profileId;

    //생성자 초기화
    public User(String name, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileId = null; //나중에 확인해야할 부분
    }

    //메서드 setter을 적용하지 않아서 그대로 둠
    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
        this.updatedAt = Instant.now();
    }

    public void setUpdatedAt() { this.updatedAt = Instant.now(); }

    public void setPassword(String password) { this.password = password; }

    public void setEmail(String email) { this.email = email; }

    public void setName(String name) { this.name = name; }
}

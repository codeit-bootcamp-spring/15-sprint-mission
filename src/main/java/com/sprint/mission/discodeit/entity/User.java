package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private String username;
    private String email;
    private String password;
    private UUID profileId;

    private final Instant createdAt;
    private Instant updatedAt;

    public User (String username,
                 String email,
                 String password,
                 UUID profileId)
    {
        this.id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void update(String username,
                       String email,
                       String password,
                       UUID profileId)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
        this.updatedAt = Instant.now();
    }
}

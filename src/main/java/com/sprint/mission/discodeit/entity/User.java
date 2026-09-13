package com.sprint.mission.discodeit.entity;
import lombok.Getter;
import java.time.Instant;
import java.util.UUID;
@Getter
public class User extends Common {
    private String nickname;
    private String username;
    private String email;
    private String password;
    private UUID profileId;
    private Instant updatedAt = getCreatedAt();
    public User(String nickname, String username, String email, String password, UUID profileId) {
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
    }
    public void update(String nickname, String username, String email, String password) {
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.password = password;
        updatedAt = Instant.now();
    }
    public void replaceProfile(UUID profileId) {
        this.profileId = profileId;
        updatedAt = Instant.now();
    }
}

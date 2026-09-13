package com.sprint.mission.discodeit.dto;
import lombok.Getter;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Getter
public class UserResponse {
    private final UUID id;
    private final String nickname;
    private final String username;
    private final String email;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final UUID profileId;
    private final boolean online;
    public UserResponse(UUID id, String nickname, String username, String email, Instant createdAt, Instant updatedAt, UUID profileId, boolean online) {
        this.id = id;
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.profileId = profileId;
        this.online = online;
    }
}

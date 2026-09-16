package com.sprint.mission.discodeit.dto;
import lombok.Getter;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Getter
public class UserUpdateRequest {
    private final UUID id;
    private final String nickname;
    private final String username;
    private final String email;
    private final String password;
    public UserUpdateRequest(UUID id, String nickname, String username, String email, String password) {
        this.id = id;
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

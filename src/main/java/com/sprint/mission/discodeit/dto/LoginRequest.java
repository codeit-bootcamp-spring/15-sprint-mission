package com.sprint.mission.discodeit.dto;
import lombok.Getter;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Getter
public class LoginRequest {
    private final String username;
    private final String password;
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class LoginResponse {
    private final UUID id;
    private final String username;

    public static LoginResponse from(User user) {
        return new LoginResponse(user.getId(), user.getUsername());
    }
}

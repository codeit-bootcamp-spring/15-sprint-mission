package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.util.UUID;

@Getter

public class UserResponse {
    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String username;
    private final String email;
    private final UUID profileId;     // BinaryContent

    private UserResponse(UUID id, Instant createdAt, Instant updatedAt, String usernamem, String email, UUID profileId){
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.username = usernamem;
        this.email = email;
        this.profileId = profileId;
    }

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId());
    }

    public static UserResponse from(UserDto userDto) {
        return new UserResponse(
                userDto.id(),
                userDto.createdAt(),
                userDto.updatedAt(),
                userDto.username(),
                userDto.email(),
                userDto.profileId());
    }
}

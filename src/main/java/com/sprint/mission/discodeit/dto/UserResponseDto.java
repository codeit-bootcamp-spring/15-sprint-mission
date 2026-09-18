package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDto(UUID id,
                              String username,
                              String email,
                              UUID profileImageId, // 프로필 BinaryContent의 ID (없으면 null)
                              boolean isOnline,    // 마지막 접속 시간이 5분 이내인지 여부
                              Instant createdAt,
                              Instant updatedAt) {

    public static UserResponseDto of(User user, boolean isOnline) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileImageId(),
                isOnline,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}

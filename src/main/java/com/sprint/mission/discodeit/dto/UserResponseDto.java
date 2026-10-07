package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    BinaryContentDto profile, // 엔티티 대신 DTO 사용
    Boolean isOnline
) {

}
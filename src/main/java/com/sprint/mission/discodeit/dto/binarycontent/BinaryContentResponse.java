package com.sprint.mission.discodeit.dto.binarycontent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponse(
        UUID id,
        String fileName,
        Long fileSize,
        String fileType,
        byte[] bytes,
        Instant createdAt,
        Instant updatedAt
) {
}
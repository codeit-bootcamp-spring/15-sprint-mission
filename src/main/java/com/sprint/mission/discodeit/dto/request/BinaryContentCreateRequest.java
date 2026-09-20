package com.sprint.mission.discodeit.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record BinaryContentCreateRequest(
        String fileName,
        String contentType,
        byte[] bytes
) {
}

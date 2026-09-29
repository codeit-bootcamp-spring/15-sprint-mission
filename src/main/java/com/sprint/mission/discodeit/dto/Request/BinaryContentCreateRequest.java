package com.sprint.mission.discodeit.dto.Request;

public record BinaryContentCreateRequest(
        String fileName,
        String contentType,
        byte[] bytes
) {
}

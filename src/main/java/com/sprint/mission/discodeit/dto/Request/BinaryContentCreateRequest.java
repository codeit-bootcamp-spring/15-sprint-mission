package com.sprint.mission.discodeit.dto.Request;

public record BinaryContentCreateRequest(
        String fileName,
        byte[] file
) {
}

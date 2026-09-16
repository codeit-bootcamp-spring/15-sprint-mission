package com.sprint.mission.discodeit.dto.binarycontent;

public record BinaryContentCreateRequest(
        String fileName,
        Long fileSize,
        String fileType,
        byte[] bytes
) {
}

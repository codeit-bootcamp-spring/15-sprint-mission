package com.sprint.mission.discodeit.dto.BinaryContentRequest;

public record BinaryContentCreateRequest (
        String fileName,
        String contentType,
        long fileSize,
        byte[] content
) {}
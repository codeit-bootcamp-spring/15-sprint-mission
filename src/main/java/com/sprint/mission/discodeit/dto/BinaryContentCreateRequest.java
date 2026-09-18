package com.sprint.mission.discodeit.dto;

public record BinaryContentCreateRequest(String fileName,
                                         Long size,
                                         String contentType,
                                         byte[] bytes) {
}

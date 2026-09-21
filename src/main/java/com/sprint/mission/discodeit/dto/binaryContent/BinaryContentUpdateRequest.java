package com.sprint.mission.discodeit.dto.binaryContent;

public record BinaryContentUpdateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

}

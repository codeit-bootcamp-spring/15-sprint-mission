package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;


@Builder
public record BinaryContentDto(
    UUID id,
    Instant createdAt,
    String fileName,
    Long size,
    String contentType
) {

}
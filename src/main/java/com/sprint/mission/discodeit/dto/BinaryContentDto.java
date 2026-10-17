package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record BinaryContentDto(
    @Schema(description = "첨부파일 ID")
    UUID id,

    @Schema(description = "원본 파일 이름")
    String fileName,

    @Schema(description = "파일 크기(byte)")
    Long size,

    @Schema(description = "파일의 MIME 타입")
    String contentType
) {

}

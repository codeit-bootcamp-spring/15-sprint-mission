package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

public record UserDto(

    @Schema(description = "사용자 ID")
    UUID id,

    @Schema(description = "사용자 생성 시각")
    Instant createdAt,

    @Schema(description = "사용자 정보 최종 수정 시각")
    Instant updatedAt,

    @Schema(description = "로그인에 사용하는 사용자 이름")
    String username,

    @Schema(description = "사용자 이메일")
    String email,

    @Schema(description = "프로필 이미지의 BinaryContent ID")
    UUID profileId,

    @Schema(description = "온라인 여부")
    Boolean online) {

}

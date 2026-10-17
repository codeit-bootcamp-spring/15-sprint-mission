package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record UserDto(

    @Schema(description = "사용자 ID")
    UUID id,

    @Schema(description = "로그인에 사용하는 사용자 이름")
    String username,

    @Schema(description = "사용자 이메일")
    String email,

    @Schema(description = "프로필 이미지 정보 (없으면 null)")
    BinaryContentDto profile,

    @Schema(description = "온라인 여부")
    Boolean online) {

}

package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public record UserUpdateRequest(

    @Schema(description = "변경할 로그인 사용자 이름")
    String newUsername,

    @Email
    @Schema(description = "변경할 이메일")
    String newEmail,

    @Schema(description = "변경할 비밀번호")
    String newPassword) {

}

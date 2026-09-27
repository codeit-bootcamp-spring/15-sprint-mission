package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserUpdateRequest(

    @Schema(description = "사용자 ID")
    @NotNull
    UUID id,

    @Schema(description = "변경할 닉네임")
    String nickname,

    @Schema(description = "변경할 로그인 사용자 이름")
    String username,

    @Email
    @Schema(description = "변경할 이메일")
    String email,

    @Schema(description = "변경할 비밀번호")
    String password) {

}

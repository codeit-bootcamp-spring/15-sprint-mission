package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

    @NotBlank
    @Schema(description = "로그인에 사용할 사용자 이름", example = "user")
    String username,

    @NotBlank
    @Schema(description = "로그인 패스워드", example = "userpassword")
    String password) {

}

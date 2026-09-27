package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(
    
    @NotBlank
    @Schema(description = "로그인에 사용할 사용자 이름", example = "username")
    String username,

    @NotBlank
    @Email
    @Schema(description = "사용자 이메일", example = "user@gmail.com")
    String email,

    @NotBlank
    @Schema(description = "로그인 비밀번호", example = "userpassword")
    String password) {

}
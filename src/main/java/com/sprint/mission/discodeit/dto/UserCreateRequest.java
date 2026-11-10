package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(

    @NotBlank
    @Size(max = 50)
    @Schema(description = "로그인에 사용할 사용자 이름", example = "newUsername")
    String username,

    @NotBlank
    @Email
    @Schema(description = "사용자 이메일", example = "user@gmail.com")
    String email,

    @NotBlank
    @Schema(description = "로그인 비밀번호", example = "userpassword")
    String password) {

}
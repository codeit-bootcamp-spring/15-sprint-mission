package com.sprint.mission.discodeit.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 아닙니다.")
        @Size(max = 100, message = "이메일은 최대 100자입니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(max = 60, message = "비밀번호는 최대 60자입니다.")
        String password,

        @NotBlank(message = "유저명은 필수입니다.")
        @Size(max = 50, message = "유저명은 최대 60자입니다.")
        String username

) {
}

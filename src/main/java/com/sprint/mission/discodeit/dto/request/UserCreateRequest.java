package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotBlank(message = "username은 비어 있을 수 없습니다.")
    @Size(max = 50, message = "username은 50자 이하여야 합니다.")
    String username,

    @NotBlank(message = "email은 비어 있을 수 없습니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    String email,

    @NotBlank(message = "password는 비어 있을 수 없습니다.")
    String password
) {

}

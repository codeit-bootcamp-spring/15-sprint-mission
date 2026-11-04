package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "유저명을 입력해주세요")
        @Size(max = 50, message = "유저명은 최대 50글자 입니다.")
        String username,

        @NotBlank(message = "비밀번호를 입력해주세요")
        @Size(max = 60, message = "비밀번호는 최대 60글자 입니다.")
        String password
) {

}

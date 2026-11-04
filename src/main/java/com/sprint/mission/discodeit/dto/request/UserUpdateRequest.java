package com.sprint.mission.discodeit.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

        @Email(message = "이메일 형식이 아닙니다.")
        @Size(max = 100, message = "이메일은 최대 100자 입니다.")
        String newEmail,

        @Size(min = 1, max = 60, message = "비밀번호는 최대 60자 입니다.")
        String newPassword,

        @Size(min = 1, max = 50, message = "유저명은 최대 50자 입니다.")
        String newUsername




) {
}
/*
UserService에서 일부가 null일 경우 나머지 필드만 업데이트하게 방어를 해놨기 때문에 해당 Dto는 일부러 @NotNull 등을 붙이지 않았습니다.
        email = userUpdateRequest.newEmail() == null ? user.getEmail() : userUpdateRequest.newEmail();
        password = userUpdateRequest.newPassword() == null ? user.getPassword() : userUpdateRequest.newPassword();
        name = userUpdateRequest.newUsername() == null ? user.getUsername() : userUpdateRequest.newUsername();
 */

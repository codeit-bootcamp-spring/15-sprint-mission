package com.sprint.mission.discodeit.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserUpdateRequest(

        @NotNull
        UUID id,

        String nickname,

        String username,

        @Email
        String email,

        String password) {
}

package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(

        @NotBlank
        String nickname,

        @NotBlank
        String username,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String password) {

}
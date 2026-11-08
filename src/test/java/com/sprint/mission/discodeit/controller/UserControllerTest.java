package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.exception.DuplicateUserNameException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
class UserControllerTest {
    @Autowired MockMvc mockMvc;
    @MockitoBean UserService userService;
    @MockitoBean UserStatusService userStatusService;

    @Test
    @DisplayName("회원가입 성공 시 201과 회원 JSON 반환")
    void 회원가입_성공() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        var request = new MockMultipartFile("userCreateRequest", "", "application/json",
                "{\"email\":\"user@example.com\",\"password\":\"password\",\"username\":\"user\"}"
                        .getBytes(StandardCharsets.UTF_8));
        when(userService.create(any(UserCreateRequest.class), any()))
                .thenReturn(new UserDto(id, "user", "user@example.com", null, true));

        // when
        var result = mockMvc.perform(multipart("/api/users").file(request));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    @DisplayName("잘못된 이메일이면 400과 필드 오류 반환")
    void 회원가입_검증실패() throws Exception {
        // given
        var request = new MockMultipartFile("userCreateRequest", "", "application/json",
                "{\"email\":\"invalid\",\"password\":\"password\",\"username\":\"user\"}"
                        .getBytes(StandardCharsets.UTF_8));

        // when
        var result = mockMvc.perform(multipart("/api/users").file(request));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT.getCode()))
                .andExpect(jsonPath("$.details.email").exists());
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("중복된 이름이면 409와 커스텀 오류 JSON 반환")
    void 회원가입_중복실패() throws Exception {
        // given
        var request = new MockMultipartFile("userCreateRequest", "", "application/json",
                "{\"email\":\"user@example.com\",\"password\":\"password\",\"username\":\"user\"}"
                        .getBytes(StandardCharsets.UTF_8));
        when(userService.create(any(UserCreateRequest.class), any()))
                .thenThrow(new DuplicateUserNameException("user"));

        // when
        var result = mockMvc.perform(multipart("/api/users").file(request));

        // then
        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATE_USER_NAME.getCode()))
                .andExpect(jsonPath("$.details.username").value("user"));
    }

    @Test
    @DisplayName("프로필 파일 검증 실패 시 400 반환")
    void 프로필_검증실패() throws Exception {
        // given
        var request = new MockMultipartFile("userCreateRequest", "", "application/json",
                "{\"email\":\"user@example.com\",\"password\":\"password\",\"username\":\"user\"}"
                        .getBytes(StandardCharsets.UTF_8));
        var profile = new MockMultipartFile("profile", "invalid", "image/png", new byte[]{1});

        // when
        var result = mockMvc.perform(multipart("/api/users").file(request).file(profile));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.fileName").exists())
                .andExpect(jsonPath("$.exceptionType").value("jakarta.validation.ConstraintViolationException"));
        verifyNoInteractions(userService);
    }
}

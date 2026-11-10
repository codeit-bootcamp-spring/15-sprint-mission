package com.sprint.mission.discodeit.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;            // when, verify, never
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;   // post, get, delete
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;     // status, jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; // andDo(print())
import static org.hamcrest.Matchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  MockMvc mockMvc;
  @MockitoBean
  UserService userService;
  @MockitoBean
  UserStatusService userStatusService;
  @MockitoBean
  BinaryContentStorage binaryContentStorage;

  @Test
  @DisplayName("유저 생성 성공 케이스")
  void user_create_success_case() throws Exception {
    UUID userId = UUID.randomUUID();
    UserDto userDto = new UserDto(
        userId,
        "username",
        "user@naver.com",
        null,
        false
    );
    String json = """
        {
          "username" : "username",
          "email" : "user@naver.com",
          "password" : "password"
        }
        """;
    MockMultipartFile userCreateRequest = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        json.getBytes(StandardCharsets.UTF_8)
    );
    when(userService.create(any(UserCreateRequest.class), isNull()))
        .thenReturn(userDto);
    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(userCreateRequest))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("username"))
        .andExpect(jsonPath("$.email").value("user@naver.com"));
  }

  @Test
  @DisplayName("유저 생성 시 필수 데이터 누락 실패 케이스")
  void user_create_fail_case() throws Exception {
    mockMvc.perform(multipart("/api/users"))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(userService);
  }

  @Test
  @DisplayName("유저 1명 정보 호출")
  void user_get_success_case() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    // when then
    mockMvc.perform(get("/api/users/{userId}", userId))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("없는 유저 조회 호출")
  void user_find_404_case() throws Exception {
    UUID userId = UUID.randomUUID();
    when(userService.find(userId)).thenThrow(new UserNotFoundException(userId));
    mockMvc.perform(get("/api/users/{userId}", userId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("USER_001"));
  }

  @Test
  @DisplayName("전체 유저 호출")
  void user_all_get_success_case() throws Exception {
    mockMvc.perform(get("/api/users/findAll"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("특정 User의 온라인 상태 수정")
  void user_status_find_success_case() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    // when & then
    mockMvc.perform(patch("/api/users/{userId}/userStatus", userId))
        .andExpect(status().isOk());
    verify(userStatusService).updateByUserId(userId);
  }

  @Test
  @DisplayName("존재하지 않는 사용자 요청 시 404 반환")
  void update_userNotFound() throws Exception {
    // given
    UUID userId = UUID.randomUUID();

    when(userStatusService.updateByUserId(userId))
        .thenThrow(new UserNotFoundException(userId));

    // when & then
    mockMvc.perform(patch("/api/users/{userId}/userStatus", userId))
        .andExpect(status().isNotFound());

    verify(userStatusService).updateByUserId(userId);
  }


}
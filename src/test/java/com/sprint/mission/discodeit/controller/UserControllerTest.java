package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  UserService userService;

  @MockitoBean
  UserStatusService userStatusService;


  // 1. 사용자 목록 조회 성공
  @Test
  void 사용자_목록_조회_성공() throws Exception {

    // given - 사용자가 없는 목록 준비
    given(userService.findAll())
        .willReturn(List.of());

    // when & then
    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }


  // 2. 사용자 삭제 성공
  @Test
  void 사용자_삭제_성공() throws Exception {

    // given
    UUID userId = UUID.randomUUID();

    // when & then
    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNoContent());

    then(userService).should().delete(userId);
  }


  // 3. 존재하지 않는 사용자 삭제 실패
  @Test
  void 사용자_삭제_실패() throws Exception {

    // given
    UUID userId = UUID.randomUUID();

    willThrow(new UserNotFoundException())
        .given(userService).delete(userId);

    // when & then
    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNotFound());
  }
}
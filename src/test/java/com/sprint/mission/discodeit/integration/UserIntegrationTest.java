package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserRepository userRepository;

  // 1. 사용자 목록 조회 성공
  @Test
  void 사용자_목록_조회_성공() throws Exception {

    // when & then
    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
  }

  // 2. 존재하지 않는 사용자 삭제 실패
  @Test
  void 사용자_삭제_실패() throws Exception {

    // given
    UUID userId = UUID.randomUUID();

    // when & then
    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNotFound());
  }

  @Test
  void 사용자_생성_성공() throws Exception {

    // given - 사용자 정보 준비
    String email = "test" + UUID.randomUUID() + "@test.com";

    String json = """
        {
          "username": "홍길동",
          "email": "%s",
          "password": "password123"
        }
        """.formatted(email);

    MockMultipartFile request = new MockMultipartFile(
        "userCreateRequest",
        "",
        "application/json",
        json.getBytes(StandardCharsets.UTF_8)
    );

    // when - 실제 사용자 생성 API 호출
    mockMvc.perform(multipart("/api/users").file(request))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("홍길동"));

    // then - H2 DB에 저장되었는지 확인
    assertTrue(userRepository.existsByEmail(email));
  }

  @Test
  void 사용자_수정_성공() throws Exception {

    // given - 기존 사용자 준비
    User user = new User("기존사용자", "before@test.com", "password123", null);
    new UserStatus(user, Instant.now());
    userRepository.saveAndFlush(user);

    UUID userId = user.getId();

    MockMultipartFile request = new MockMultipartFile(
        "userUpdateRequest",
        "",
        "application/json",
        """
            {
              "newUsername": "수정된사용자",
              "newEmail": "after@test.com",
              "newPassword": "password456"
            }
            """.getBytes(StandardCharsets.UTF_8)
    );

    // when - 사용자 수정 API 호출
    mockMvc.perform(multipart("/api/users/{userId}", userId)
            .file(request)
            .with(req -> {
              req.setMethod("PATCH");
              return req;
            }))
        .andExpect(status().isOk());

    // then - 실제 수정 결과 확인
    User updated = userRepository.findById(userId).orElseThrow();

    assertEquals("수정된사용자", updated.getUsername());
    assertEquals("after@test.com", updated.getEmail());
  }

  @Test
  void 사용자_목록_조회_성공_데이터있음() throws Exception {

    // given - 테스트 사용자 생성
    User user = new User(
        "목록사용자", "list@test.com", "password123", null
    );

    new UserStatus(user, Instant.now());
    userRepository.saveAndFlush(user);

    // when & then - 사용자 목록 조회
    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].username").value("목록사용자"))
        .andExpect(jsonPath("$[0].email").value("list@test.com"));
  }


}
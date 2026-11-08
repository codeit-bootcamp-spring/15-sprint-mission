package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserRepository userRepository;

  @TestConfiguration
  @EnableJpaAuditing
  static class AuditingTestConfig {

  }

  private MockMultipartFile userRequest(String username, String email) {
    String json = """
        {
          "username": "%s",
          "email": "%s",
          "password": "password"
        }
        """.formatted(username, email);
    return new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        json.getBytes(StandardCharsets.UTF_8)
    );
  }

  private UUID createUser(String username, String email) throws Exception {
    MvcResult result = mockMvc.perform(multipart("/api/users")
            .file(userRequest(username, email)))
        .andExpect(status().isCreated())
        .andReturn();
    String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    return UUID.fromString(id);
  }

  @Test
  @DisplayName("사용자 생성 성공")
  void user_create_success_case() throws Exception {
    // given
    long beforeCount = userRepository.count();
    String json = """
        {
          "username": "username",
          "email": "user@naver.com",
          "password": "password"
        }
        """;
    MockMultipartFile request = new MockMultipartFile(
        "userCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        json.getBytes(StandardCharsets.UTF_8)
    );
    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(request))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.username").value("username"))
        .andExpect(jsonPath("$.email").value("user@naver.com"));

    assertThat(userRepository.count()).isEqualTo(beforeCount + 1);
  }

  @Test
  @DisplayName("사용자 생성 실패 - 필수 요청 데이터 누락")
  void user_create_fail_missing_request() throws Exception {
    // when & then
    mockMvc.perform(multipart("/api/users"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("사용자 생성 실패 - 이메일 중복")
  void user_create_fail_duplicate_email() throws Exception {
    // given
    createUser("user01", "user@naver.com");
    long beforeCount = userRepository.count();
    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(userRequest("user02", "user@naver.com")))
        .andExpect(status().isConflict());
    assertThat(userRepository.count()).isEqualTo(beforeCount);
  }

  @Test
  @DisplayName("사용자 삭제 성공")
  void user_delete_success_case() throws Exception {
    // given
    UUID userId = createUser("user01", "user01@naver.com");
    // when & then
    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNoContent());
    assertThat(userRepository.existsById(userId)).isFalse();
  }

  @Test
  @DisplayName("사용자 목록 조회 성공")
  void user_find_all_success_case() throws Exception {
    // given
    createUser("user01", "user01@naver.com");
    createUser("user02", "user02@naver.com");
    // when & then
    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].username",
            hasItems("user01", "user02")))
        .andExpect(jsonPath("$[*].email",
            hasItems("user01@naver.com", "user02@naver.com")));
  }


}

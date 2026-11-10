package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.sprint.mission.discodeit.repository.MessageRepository;
import jakarta.persistence.EntityManager;
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
class MessageTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private EntityManager entityManager;

  @TestConfiguration
  @EnableJpaAuditing
  static class AuditingTestConfig {

  }

  // 사용자 생성
  private UUID createUser() throws Exception {
    String json = """
        {
          "username": "user01",
          "email": "user01@naver.com",
          "password": "password"
        }
        """;
    MockMultipartFile request = new MockMultipartFile(
        "userCreateRequest", "",
        MediaType.APPLICATION_JSON_VALUE,
        json.getBytes(StandardCharsets.UTF_8)
    );
    MvcResult result = mockMvc.perform(multipart("/api/users")
            .file(request))
        .andExpect(status().isCreated())
        .andReturn();
    String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    return UUID.fromString(id);
  }

  // PUBLIC 채널 생성
  private UUID createChannel() throws Exception {
    String json = """
        {
          "name": "channelName",
          "description": "channelDesc"
        }
        """;
    MvcResult result = mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andReturn();
    String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

    return UUID.fromString(id);
  }

  // 메시지 생성
  private UUID createMessage(UUID authorId, UUID channelId) throws Exception {
    String json = """
        {
          "authorId": "%s",
          "channelId": "%s",
          "content": "테스트 메시지"
        }
        """.formatted(authorId, channelId);
    MockMultipartFile request = new MockMultipartFile(
        "messageCreateRequest", "",
        MediaType.APPLICATION_JSON_VALUE,
        json.getBytes(StandardCharsets.UTF_8)
    );
    MvcResult result = mockMvc.perform(multipart("/api/messages")
            .file(request))
        .andExpect(status().isCreated())
        .andReturn();
    String id = JsonPath.read(
        result.getResponse().getContentAsString(), "$.id");
    return UUID.fromString(id);
  }

  @Test
  @DisplayName("메시지 생성 성공")
  void message_create_success_case() throws Exception {
    // given
    UUID authorId = createUser();
    UUID channelId = createChannel();
    long beforeCount = messageRepository.count();
    // when
    UUID messageId = createMessage(authorId, channelId);
    // then
    assertThat(messageRepository.existsById(messageId)).isTrue();
    assertThat(messageRepository.count()).isEqualTo(beforeCount + 1);
  }

  @Test
  @DisplayName("메시지 생성 실패 - 필수 요청 누락")
  void message_create_fail_missing_request() throws Exception {
    // given
    long beforeCount = messageRepository.count();
    // when & then
    mockMvc.perform(multipart("/api/messages"))
        .andExpect(status().isBadRequest());
    assertThat(messageRepository.count()).isEqualTo(beforeCount);
  }

  @Test
  @DisplayName("메시지 수정 성공")
  void message_update_success_case() throws Exception {
    // given
    UUID authorId = createUser();
    UUID channelId = createChannel();
    UUID messageId = createMessage(authorId, channelId);
    String json = """
        {
          "content": "수정된 메시지"
        }
        """;
    // when & then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value("수정된 메시지"));

    entityManager.flush();
    entityManager.clear();

    assertThat(messageRepository.findById(messageId).orElseThrow().getContent())
        .isEqualTo("수정된 메시지");
  }

  @Test
  @DisplayName("메시지 수정 실패 - 존재하지 않는 메시지")
  void message_update_fail_not_found() throws Exception {
    // given
    UUID messageId = UUID.randomUUID();
    String json = """
        {
          "content": "수정된 메시지"
        }
        """;
    // when & then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isNotFound());
    assertThat(messageRepository.existsById(messageId)).isFalse();
  }

  @Test
  @DisplayName("메시지 삭제 성공")
  void message_delete_success_case() throws Exception {
    // given
    UUID authorId = createUser();
    UUID channelId = createChannel();
    UUID messageId = createMessage(authorId, channelId);
    // when & then
    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNoContent());

    entityManager.flush();
    entityManager.clear();

    assertThat(messageRepository.existsById(messageId)).isFalse();
  }

  @Test
  @DisplayName("채널 메시지 목록 조회 성공")
  void message_find_all_success_case() throws Exception {
    // given
    UUID authorId = createUser();
    UUID channelId = createChannel();
    createMessage(authorId, channelId);
    // when & then
    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString()))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("테스트 메시지")));
  }

}

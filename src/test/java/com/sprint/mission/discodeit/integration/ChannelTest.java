package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.sprint.mission.discodeit.repository.ChannelRepository;
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
class ChannelTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ChannelRepository channelRepository;
  @Autowired
  private EntityManager entityManager;

  @TestConfiguration
  @EnableJpaAuditing
  static class AuditingTestConfig {

  }

  // PUBLIC 채널 생성
  private UUID createPublicChannel() throws Exception {
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

  // PRIVATE 채널 생성에 필요한 사용자 준비
  private UUID createUser(String username, String email) throws Exception {
    String json = """
        {
          "username": "%s",
          "email": "%s",
          "password": "password"
        }
        """.formatted(username, email);
    MockMultipartFile request = new MockMultipartFile(
        "userCreateRequest", "",
        MediaType.APPLICATION_JSON_VALUE,
        json.getBytes(StandardCharsets.UTF_8)
    );
    MvcResult result = mockMvc.perform(multipart("/api/users")
            .file(request))
        .andExpect(status().isCreated())
        .andReturn();
    String id = JsonPath.read(
        result.getResponse().getContentAsString(), "$.id");
    return UUID.fromString(id);
  }

  @Test
  @DisplayName("PUBLIC 채널 생성 성공")
  void public_channel_create_success_case() throws Exception {
    // given
    long beforeCount = channelRepository.count();
    String json = """
        {
          "name": "channelName",
          "description": "channelDesc"
        }
        """;
    // when & then
    MvcResult result = mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.type").value("PUBLIC"))
        .andExpect(jsonPath("$.name").value("channelName"))
        .andExpect(jsonPath("$.description").value("channelDesc"))
        .andReturn();
    String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    UUID channelId = UUID.fromString(id);
    assertThat(channelRepository.existsById(channelId)).isTrue();
    assertThat(channelRepository.count()).isEqualTo(beforeCount + 1);
  }

  @Test
  @DisplayName("PRIVATE 채널 생성 성공")
  void private_channel_create_success_case() throws Exception {
    // given
    UUID userId1 = createUser("user01", "user01@naver.com");
    UUID userId2 = createUser("user02", "user02@naver.com");
    String json = """
        {
          "participantIds": ["%s", "%s"]
        }
        """.formatted(userId1, userId2);
    // when & then
    MvcResult result = mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.type").value("PRIVATE"))
        .andReturn();

    String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    assertThat(channelRepository.existsById(UUID.fromString(id))).isTrue();
  }

  @Test
  @DisplayName("PUBLIC 채널 수정 성공")
  void public_channel_update_success_case() throws Exception {
    // given
    UUID channelId = createPublicChannel();
    String json = """
        {
          "name": "newName",
          "description": "newDesc"
        }
        """;
    // when & then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("newName"))
        .andExpect(jsonPath("$.description").value("newDesc"));

    entityManager.flush();
    entityManager.clear();

    assertThat(channelRepository.findById(channelId).orElseThrow().getName())
        .isEqualTo("newName");
  }

  @Test
  @DisplayName("PUBLIC 채널 수정 실패 - 이름 길이 초과")
  void public_channel_update_fail_invalid_name() throws Exception {
    // given
    UUID channelId = createPublicChannel();
    String invalidName = "a".repeat(101);
    String json = """
        {
          "name": "%s",
          "description": "newDesc"
        }
        """.formatted(invalidName);
    // when & then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details.name").exists());
    assertThat(channelRepository.findById(channelId).orElseThrow().getName())
        .isEqualTo("channelName");
  }

  @Test
  @DisplayName("채널 삭제 성공")
  void channel_delete_success_case() throws Exception {
    // given
    UUID channelId = createPublicChannel();
    // when & then
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());

    entityManager.flush();
    entityManager.clear();

    assertThat(channelRepository.existsById(channelId)).isFalse();
  }

}
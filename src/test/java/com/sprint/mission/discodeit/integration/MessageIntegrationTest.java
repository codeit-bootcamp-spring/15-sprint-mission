package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import jakarta.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MessageIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  MessageRepository messageRepository;

  @Autowired
  ChannelRepository channelRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  EntityManager entityManager;

  // 1. 메시지 생성 성공
  @Test
  void 메시지_생성_성공() throws Exception {

    // given - 채널 생성
    Channel channel = channelRepository.saveAndFlush(
        new Channel(ChannelType.PUBLIC, "테스트 채널", "설명")
    );

    // given - 메시지 작성자 생성
    User user = new User(
        "작성자", "author@test.com", "password123", null
    );

    new UserStatus(user, Instant.now());
    userRepository.saveAndFlush(user);

    // given - 메시지 생성 요청
    String json = """
        {
          "content": "안녕하세요",
          "channelId": "%s",
          "authorId": "%s"
        }
        """.formatted(channel.getId(), user.getId());

    MockMultipartFile request = new MockMultipartFile(
        "messageCreateRequest",
        "request.json",
        MediaType.APPLICATION_JSON_VALUE,
        json.getBytes(StandardCharsets.UTF_8)
    );

    // when - 메시지 생성 API 실행
    mockMvc.perform(multipart("/api/messages").file(request))
        .andExpect(status().isCreated());

    // then - 실제 H2 DB에 저장되었는지 확인
    messageRepository.flush();

    assertTrue(messageRepository.findAll().stream()
        .anyMatch(message ->
            message.getContent().equals("안녕하세요")));
  }

  @Test
  void 메시지_수정_성공() throws Exception {

    // given - 채널 생성
    Channel channel = channelRepository.saveAndFlush(
        new Channel(ChannelType.PUBLIC, "테스트 채널", "설명")
    );

    // given - 사용자 생성
    User user = new User("작성자", "author@test.com", "123456", null);
    new UserStatus(user, Instant.now());
    userRepository.saveAndFlush(user);

    // given - 기존 메시지 저장
    Message message = messageRepository.saveAndFlush(
        new Message("수정 전 메시지", channel, user, List.of())
    );

    UUID messageId = message.getId();

    // when - 메시지 수정 API 실행
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "newContent": "수정 후 메시지"
                }
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value("수정 후 메시지"));

    // then - DB에 변경된 내용 확인
    messageRepository.flush();
    entityManager.clear();

    Message updated = messageRepository.findById(messageId).orElseThrow();

    assertEquals("수정 후 메시지", updated.getContent());
  }

  @Test
  void 메시지_삭제_성공() throws Exception {

    // given - 채널 생성
    Channel channel = channelRepository.saveAndFlush(
        new Channel(ChannelType.PUBLIC, "테스트 채널", "설명")
    );

    // given - 사용자 생성
    User user = new User("작성자", "author@test.com", "123456", null);
    new UserStatus(user, Instant.now());
    userRepository.saveAndFlush(user);

    // given - 삭제할 메시지 생성
    Message message = messageRepository.saveAndFlush(
        new Message("삭제할 메시지", channel, user, List.of())
    );

    UUID messageId = message.getId();

    // when - 메시지 삭제 API 실행
    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNoContent());

    // then - 실제 DB에서 삭제되었는지 확인
    messageRepository.flush();
    entityManager.clear();

    assertFalse(messageRepository.existsById(messageId));
  }

  @Test
  void 메시지_삭제_실패() throws Exception {

    // given - 존재하지 않는 메시지 ID
    UUID messageId = UUID.randomUUID();

    // when & then - 삭제 실패 확인
    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message")
            .value("메시지를 찾을 수 없습니다."));

    // then - DB에도 메시지가 없는지 확인
    assertFalse(messageRepository.existsById(messageId));
  }

  @Test
  void 채널별_메시지_목록_조회_성공() throws Exception {

    // given - 채널 생성
    Channel channel = channelRepository.saveAndFlush(
        new Channel(ChannelType.PUBLIC, "조회 테스트 채널", "설명")
    );

    // given - 작성자 생성
    User user = new User("작성자", "author@test.com", "123456", null);
    new UserStatus(user, Instant.now());
    userRepository.saveAndFlush(user);

    // given - 메시지 저장
    messageRepository.saveAndFlush(
        new Message("조회 테스트 메시지", channel, user, List.of())
    );

    // when & then - 메시지 목록 조회
    mockMvc.perform(get("/api/messages")
            .param("channelId", channel.getId().toString())
            .param("cursor", Instant.now().plusSeconds(60).toString())
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(content().string(
            containsString("조회 테스트 메시지")
        ));
  }

  @Test
  void 채널별_메시지_목록_조회_빈목록() throws Exception {

    // given - 메시지가 없는 채널 생성
    Channel channel = channelRepository.saveAndFlush(
        new Channel(ChannelType.PUBLIC, "빈 채널", "메시지 없음")
    );

    // when & then - 메시지 목록 조회
    mockMvc.perform(get("/api/messages")
            .param("channelId", channel.getId().toString())
            .param("cursor", Instant.now().plusSeconds(60).toString())
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isEmpty());
  }
  

}
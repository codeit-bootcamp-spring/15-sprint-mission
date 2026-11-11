package com.sprint.mission.discodeit.integration;

import com.jayway.jsonpath.JsonPath;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MessageApiIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired UserRepository userRepository;
    @Autowired ChannelRepository channelRepository;
    @Autowired MessageRepository messageRepository;
    @Autowired EntityManager entityManager;
    User user;
    Channel channel;

    @BeforeEach
    void setUp() {
        user = userRepository.saveAndFlush(new User("user@example.com", "password", "user", null));
        channel = channelRepository.saveAndFlush(new Channel("general", "description", ChannelType.PUBLIC));
    }

    @Test
    @DisplayName("메시지 생성 API가 작성자와 채널을 연결해 DB에 저장")
    void 메시지생성_성공() throws Exception {
        // given
        var request = messagePart(channel.getId());

        // when
        var result = mockMvc.perform(multipart("/api/messages").file(request));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("hello"))
                .andExpect(jsonPath("$.author.id").value(user.getId().toString()));
        String id = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.id");
        entityManager.flush();
        entityManager.clear();
        Message saved = messageRepository.findById(UUID.fromString(id)).orElseThrow();
        assertThat(saved.getChannel().getId()).isEqualTo(channel.getId());
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("없는 채널에 메시지 생성 시 404 반환")
    void 메시지생성_실패() throws Exception {
        // given
        var request = messagePart(UUID.randomUUID());

        // when
        var result = mockMvc.perform(multipart("/api/messages").file(request));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.CHANNEL_NOT_FOUND.getCode()));
    }

    @Test
    @DisplayName("메시지 수정 API가 변경된 본문을 DB에 반영")
    void 메시지수정_성공() throws Exception {
        // given
        Message message = messageRepository.saveAndFlush(new Message(channel, user, "hello", List.of()));

        // when
        var result = mockMvc.perform(patch("/api/messages/{id}", message.getId())
                .contentType(MediaType.APPLICATION_JSON).content("{\"newContent\":\"updated\"}"));

        // then
        result.andExpect(status().isOk()).andExpect(jsonPath("$.content").value("updated"));
        entityManager.flush();
        entityManager.clear();
        assertThat(messageRepository.findById(message.getId()).orElseThrow().getContent()).isEqualTo("updated");
    }

    @Test
    @DisplayName("없는 메시지 수정 시 404 반환")
    void 메시지수정_실패() throws Exception {
        // given
        UUID id = UUID.randomUUID();

        // when
        var result = mockMvc.perform(patch("/api/messages/{id}", id)
                .contentType(MediaType.APPLICATION_JSON).content("{\"newContent\":\"updated\"}"));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.MESSAGE_NOT_FOUND.getCode()));
    }

    @Test
    @DisplayName("메시지 삭제 API가 DB에서 메시지 삭제")
    void 메시지삭제_성공() throws Exception {
        // given
        Message message = messageRepository.saveAndFlush(new Message(channel, user, "hello", List.of()));
        UUID id = message.getId();

        // when
        var result = mockMvc.perform(delete("/api/messages/{id}", id));

        // then
        result.andExpect(status().isNoContent());
        entityManager.flush();
        entityManager.clear();
        assertThat(messageRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("없는 메시지 삭제 시 404 반환")
    void 메시지삭제_실패() throws Exception {
        // given
        UUID id = UUID.randomUUID();

        // when
        var result = mockMvc.perform(delete("/api/messages/{id}", id));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.details.messageId").value(id.toString()));
    }

    @Test
    @DisplayName("메시지 목록 API에서 커서로 다음 페이지 조회")
    void 메시지목록_조회_성공() throws Exception {
        // given
        Instant latest = Instant.parse("2026-01-03T00:00:00Z");
        for (int i = 0; i < 2; i++) {
            Message message = messageRepository.saveAndFlush(new Message(channel, user, "message-" + i, List.of()));
            entityManager.createQuery("update Message m set m.createdAt = :time where m.id = :id")
                    .setParameter("time", latest.minusSeconds(i)).setParameter("id", message.getId()).executeUpdate();
        }
        entityManager.clear();

        // when
        var first = mockMvc.perform(get("/api/messages").param("channelId", channel.getId().toString())
                .param("size", "1"));
        String cursor = JsonPath.read(first.andReturn().getResponse().getContentAsString(), "$.nextCursor");
        var second = mockMvc.perform(get("/api/messages").param("channelId", channel.getId().toString())
                .param("size", "1").param("cursor", cursor));

        // then
        first.andExpect(status().isOk()).andExpect(jsonPath("$.content[0].content").value("message-0"))
                .andExpect(jsonPath("$.hasNext").value(true)).andExpect(jsonPath("$.totalElements").value(2));
        second.andExpect(status().isOk()).andExpect(jsonPath("$.content[0].content").value("message-1"))
                .andExpect(jsonPath("$.hasNext").value(false)).andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @DisplayName("메시지가 없는 채널은 빈 페이지 반환")
    void 메시지목록_조회_결과없음() throws Exception {
        // given
        UUID channelId = channel.getId();

        // when
        var result = mockMvc.perform(get("/api/messages").param("channelId", channelId.toString()));

        // then
        result.andExpect(status().isOk()).andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.hasNext").value(false)).andExpect(jsonPath("$.totalElements").value(0));
    }

    private MockMultipartFile messagePart(UUID channelId) {
        String json = "{\"channelId\":\"%s\",\"authorId\":\"%s\",\"content\":\"hello\"}"
                .formatted(channelId, user.getId());
        return new MockMultipartFile("messageCreateRequest", "", "application/json", json.getBytes(StandardCharsets.UTF_8));
    }
}

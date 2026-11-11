package com.sprint.mission.discodeit.integration;

import com.jayway.jsonpath.JsonPath;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ChannelApiIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired ChannelRepository channelRepository;
    @Autowired UserRepository userRepository;
    @Autowired ReadStatusRepository readStatusRepository;
    @Autowired EntityManager entityManager;

    @Test
    @DisplayName("공개 채널 생성 API가 DB에 공개 채널 저장")
    void 공개채널_생성_성공() throws Exception {
        // given
        String request = "{\"name\":\"general\",\"description\":\"description\"}";

        // when
        var result = mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON).content(request));

        // then
        result.andExpect(status().isCreated()).andExpect(jsonPath("$.type").value("PUBLIC"));
        String id = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.id");
        entityManager.flush();
        entityManager.clear();
        assertThat(channelRepository.findById(UUID.fromString(id)).orElseThrow().getName()).isEqualTo("general");
    }

    @Test
    @DisplayName("빈 공개 채널명은 검증 오류로 400 반환")
    void 공개채널_생성_실패() throws Exception {
        // given
        String request = "{\"name\":\"\"}";

        // when
        var result = mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON).content(request));

        // then
        result.andExpect(status().isBadRequest()).andExpect(jsonPath("$.details.name").exists());
        assertThat(channelRepository.count()).isZero();
    }

    @Test
    @DisplayName("비공개 채널 생성 API가 참여자의 읽음 상태도 저장")
    void 비공개채널_생성_성공() throws Exception {
        // given
        User user = userRepository.saveAndFlush(new User("user@example.com", "password", "user", null));
        String request = "{\"participantIds\":[\"%s\"]}".formatted(user.getId());

        // when
        var result = mockMvc.perform(post("/api/channels/private")
                .contentType(MediaType.APPLICATION_JSON).content(request));

        // then
        result.andExpect(status().isCreated()).andExpect(jsonPath("$.type").value("PRIVATE"))
                .andExpect(jsonPath("$.participants[0].id").value(user.getId().toString()));
        String id = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.id");
        entityManager.flush();
        entityManager.clear();
        assertThat(readStatusRepository.existsByUserIdAndChannelId(user.getId(), UUID.fromString(id))).isTrue();
    }

    @Test
    @DisplayName("참여자가 없는 비공개 채널 생성 시 400 반환")
    void 비공개채널_생성_실패() throws Exception {
        // given
        String request = "{\"participantIds\":[]}";

        // when
        var result = mockMvc.perform(post("/api/channels/private")
                .contentType(MediaType.APPLICATION_JSON).content(request));

        // then
        result.andExpect(status().isBadRequest()).andExpect(jsonPath("$.details.participantIds").exists());
        assertThat(channelRepository.count()).isZero();
    }

    @Test
    @DisplayName("공개 채널 수정 API가 변경값을 DB에 반영")
    void 채널수정_성공() throws Exception {
        // given
        Channel channel = channelRepository.saveAndFlush(new Channel("general", "old", ChannelType.PUBLIC));

        // when
        var result = mockMvc.perform(patch("/api/channels/{id}", channel.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"updated\",\"description\":\"new\"}"));

        // then
        result.andExpect(status().isOk()).andExpect(jsonPath("$.name").value("updated"));
        entityManager.flush();
        entityManager.clear();
        assertThat(channelRepository.findById(channel.getId()).orElseThrow().getDescription()).isEqualTo("new");
    }

    @Test
    @DisplayName("비공개 채널 수정은 422 반환")
    void 채널수정_실패() throws Exception {
        // given
        Channel channel = channelRepository.saveAndFlush(new Channel(null, null, ChannelType.PRIVATE));

        // when
        var result = mockMvc.perform(patch("/api/channels/{id}", channel.getId())
                .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"updated\"}"));

        // then
        result.andExpect(status().is(422))
                .andExpect(jsonPath("$.code").value(ErrorCode.PRIVATE_CHANNEL_UPDATE.getCode()));
    }

    @Test
    @DisplayName("채널 삭제 API가 DB에서 채널 삭제")
    void 채널삭제_성공() throws Exception {
        // given
        Channel channel = channelRepository.saveAndFlush(new Channel("general", "description", ChannelType.PUBLIC));
        UUID id = channel.getId();

        // when
        var result = mockMvc.perform(delete("/api/channels/{id}", id));

        // then
        result.andExpect(status().isNoContent());
        entityManager.flush();
        entityManager.clear();
        assertThat(channelRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("없는 채널 삭제 시 404 반환")
    void 채널삭제_실패() throws Exception {
        // given
        UUID id = UUID.randomUUID();

        // when
        var result = mockMvc.perform(delete("/api/channels/{id}", id));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.CHANNEL_NOT_FOUND.getCode()));
    }
}

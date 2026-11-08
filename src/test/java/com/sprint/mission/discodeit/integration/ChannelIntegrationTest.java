package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
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
class ChannelIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ChannelRepository channelRepository;

  // 1. 공개 채널 생성 성공
  @Test
  void 공개채널_생성_성공() throws Exception {

    // given - 생성할 채널 정보
    String json = """
        {
          "name": "테스트 공개채널",
          "description": "채널 통합 테스트"
        }
        """;

    // when - 채널 생성 API 실행
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("테스트 공개채널"));

    // then - H2 DB에 실제 저장되었는지 확인
    channelRepository.flush();

    assertTrue(channelRepository.findAll().stream()
        .anyMatch(channel ->
            channel.getName().equals("테스트 공개채널")));
  }

  @Test
  void 공개채널_수정_성공() throws Exception {

    // given - 기존 공개 채널 생성
    Channel channel = new Channel(
        ChannelType.PUBLIC, "기존 채널명", "기존 설명"
    );

    channelRepository.saveAndFlush(channel);
    UUID channelId = channel.getId();

    // when - 채널 수정 API 호출
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "newName": "수정된 채널명",
                  "newDescription": "수정된 설명"
                }
                """))
        .andExpect(status().isOk());

    // then - H2 DB의 수정 결과 확인
    channelRepository.flush();

    Channel updated = channelRepository.findById(channelId)
        .orElseThrow();

    assertEquals("수정된 채널명", updated.getName());
    assertEquals("수정된 설명", updated.getDescription());
  }

  @Test
  void 공개채널_삭제_성공() throws Exception {

    // given - 삭제할 채널 생성
    Channel channel = new Channel(
        ChannelType.PUBLIC, "삭제할 채널", "테스트 설명"
    );

    channelRepository.saveAndFlush(channel);
    UUID channelId = channel.getId();

    // when - 채널 삭제 API 실행
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());

    // then - 실제 DB에서 삭제되었는지 확인
    channelRepository.flush();

    assertFalse(channelRepository.existsById(channelId));
  }

  @Test
  void 공개채널_삭제_실패() throws Exception {

    // given - 존재하지 않는 채널 ID
    UUID channelId = UUID.randomUUID();

    // when & then - 삭제 실패 확인
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message")
            .value("채널을 찾을 수 없습니다."));

    // then - DB에도 채널이 없는지 확인
    assertFalse(channelRepository.existsById(channelId));
  }
}
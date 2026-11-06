
package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChannelController.class)
@ActiveProfiles("test")
class ChannelControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  ChannelService channelService;


  // 1. 공개 채널 생성 성공
  @Test
  void 공개채널_생성_성공() throws Exception {

    // when - 공개 채널 생성 요청
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "name": "공지사항",
                  "description": "테스트 채널"
                }
                """))
        .andExpect(status().isCreated());

    // then - Service 호출 확인
    then(channelService).should()
        .create(any(PublicChannelCreateRequest.class));
  }


  // 2. 공개 채널 생성 실패 - 이름 누락
  @Test
  void 공개채널_생성_실패() throws Exception {

    // when & then - 잘못된 요청
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "name": "",
                  "description": "테스트 채널"
                }
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
  }


  // 3. 비공개 채널 생성 성공
  @Test
  void 비공개채널_생성_성공() throws Exception {

    // given - 참여자 2명
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();

    String requestJson = """
        {
          "participantIds": ["%s", "%s"]
        }
        """.formatted(userId1, userId2);

    // when
    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isCreated());

    // then
    then(channelService).should()
        .create(any(PrivateChannelCreateRequest.class));
  }


  // 4. 공개 채널 수정 성공
  @Test
  void 공개채널_수정_성공() throws Exception {

    // given
    UUID channelId = UUID.randomUUID();

    // when
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "newName": "수정된 채널",
                  "newDescription": "수정된 설명"
                }
                """))
        .andExpect(status().isOk());

    // then
    then(channelService).should()
        .update(eq(channelId), any(PublicChannelUpdateRequest.class));
  }


  // 5. 채널 삭제 성공
  @Test
  void 채널_삭제_성공() throws Exception {

    // given
    UUID channelId = UUID.randomUUID();

    // when
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());

    // then
    then(channelService).should().delete(channelId);
  }


  // 6. 채널 삭제 실패
  @Test
  void 채널_삭제_실패() throws Exception {

    // given - 존재하지 않는 채널
    UUID channelId = UUID.randomUUID();

    willThrow(new ChannelNotFoundException())
        .given(channelService).delete(channelId);

    // when & then
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNotFound());
  }


  // 7. 사용자별 채널 조회 - 빈 JSON 목록
  @Test
  void 채널_목록_조회_성공() throws Exception {

    // given
    UUID userId = UUID.randomUUID();

    given(channelService.findAllByUserId(userId))
        .willReturn(List.of());

    // when & then
    mockMvc.perform(get("/api/channels")
            .param("userId", userId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }
}


package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
@ActiveProfiles("test")
class MessageControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  MessageService messageService;


  // 1. 메시지 생성 성공
  @Test
  void 메시지_생성_성공() throws Exception {

    // given - 메시지 생성 요청 준비
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();

    MockMultipartFile request = new MockMultipartFile(
        "messageCreateRequest",
        "",
        "application/json",
        """
            {
              "content": "안녕하세요",
              "channelId": "%s",
              "authorId": "%s"
            }
            """.formatted(channelId, authorId)
            .getBytes(StandardCharsets.UTF_8)
    );

    // when & then - HTTP 201 확인
    mockMvc.perform(multipart("/api/messages").file(request))
        .andExpect(status().isCreated());

    then(messageService).should()
        .create(any(MessageCreateRequest.class), anyList());
  }


  // 2. 메시지 생성 실패 - 채널 없음
  @Test
  void 메시지_생성_실패() throws Exception {

    // given
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();

    MockMultipartFile request = new MockMultipartFile(
        "messageCreateRequest",
        "",
        "application/json",
        """
            {
              "content": "안녕하세요",
              "channelId": "%s",
              "authorId": "%s"
            }
            """.formatted(channelId, authorId)
            .getBytes(StandardCharsets.UTF_8)
    );

    given(messageService.create(
        any(MessageCreateRequest.class), anyList()))
        .willThrow(new ChannelNotFoundException());

    // when & then - HTTP 404 확인
    mockMvc.perform(multipart("/api/messages").file(request))
        .andExpect(status().isNotFound());
  }


  // 3. 메시지 수정 성공
  @Test
  void 메시지_수정_성공() throws Exception {

    // given
    UUID messageId = UUID.randomUUID();

    // when & then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "newContent": "수정된 메시지"
                }
                """))
        .andExpect(status().isOk());

    then(messageService).should()
        .update(eq(messageId), any(MessageUpdateRequest.class));
  }


  // 4. 메시지 수정 실패
  @Test
  void 메시지_수정_실패() throws Exception {

    // given
    UUID messageId = UUID.randomUUID();

    given(messageService.update(
        eq(messageId), any(MessageUpdateRequest.class)))
        .willThrow(new MessageNotFoundException());

    // when & then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "newContent": "수정된 메시지"
                }
                """))
        .andExpect(status().isNotFound());
  }


  // 5. 메시지 삭제 성공
  @Test
  void 메시지_삭제_성공() throws Exception {

    // given
    UUID messageId = UUID.randomUUID();

    // when & then
    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNoContent());

    then(messageService).should().delete(messageId);
  }


  // 6. 메시지 삭제 실패 및 JSON 응답 검증
  @Test
  void 메시지_삭제_실패() throws Exception {

    // given
    UUID messageId = UUID.randomUUID();

    willThrow(new MessageNotFoundException())
        .given(messageService).delete(messageId);

    // when & then
    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.message")
            .value("메시지를 찾을 수 없습니다."));
  }


  // 7. 채널별 메시지 조회 성공
  @Test
  void 채널별_메시지_조회_성공() throws Exception {

    // given
    UUID channelId = UUID.randomUUID();

    // when & then
    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString())
            .param("size", "10"))
        .andExpect(status().isOk());

    then(messageService).should()
        .findAllByChannelId(
            eq(channelId),
            isNull(),
            argThat(pageable -> pageable.getPageSize() == 10)
        );
  }
}

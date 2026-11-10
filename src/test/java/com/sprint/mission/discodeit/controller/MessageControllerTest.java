package com.sprint.mission.discodeit.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
@ActiveProfiles("test")
class MessageControllerTest {

  @Autowired
  MockMvc mockMvc;
  @MockitoBean
  MessageService messageService;

  @Test
  @DisplayName("메세지 생성 성공 케이스")
  void message_create_success_case() throws Exception {
    // given
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();

    String json = """
        {
          "authorId": "%s",
          "channelId": "%s",
          "content": "안녕하세요"
        }
        """.formatted(authorId, channelId);
    MockMultipartFile request = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        json.getBytes(StandardCharsets.UTF_8)
    );
    MessageDto messageDto = mock(MessageDto.class);
    when(messageService.create(any(MessageCreateRequest.class), anyList())).thenReturn(messageDto);
    // when & then
    mockMvc.perform(multipart("/api/messages")
            .file(request))
        .andExpect(status().isCreated());

    verify(messageService).create(
        any(MessageCreateRequest.class),
        argThat(List::isEmpty)
    );
  }

  @Test
  @DisplayName("메시지 생성 실패 - 필수 요청 데이터 누락")
  void message_create_fail_missing_request() throws Exception {
    // when & then
    mockMvc.perform(multipart("/api/messages"))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(messageService);
  }

  @Test
  @DisplayName("메시지 수정 성공")
  void message_update_success_case() throws Exception {
    // given
    UUID messageId = UUID.randomUUID();
    MessageDto messageDto = mock(MessageDto.class);
    when(messageDto.id()).thenReturn(messageId);
    when(messageDto.content()).thenReturn("수정된 내용");
    String json = """
        {
          "content": "수정된 내용"
        }
        """;
    when(messageService.update(eq(messageId), any(MessageUpdateRequest.class)))
        .thenReturn(messageDto);
    // when & then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value("수정된 내용"));

    verify(messageService).update(
        eq(messageId),
        argThat(request -> "수정된 내용".equals(request.content()))
    );
  }

  @Test
  @DisplayName("메시지 수정 실패 - 존재하지 않는 메시지")
  void message_update_fail_not_found() throws Exception {
    // given
    UUID messageId = UUID.randomUUID();
    String json = """
        {
          "content": "수정된 내용"
        }
        """;
    when(messageService.update(eq(messageId), any(MessageUpdateRequest.class)))
        .thenThrow(new MessageNotFoundException(messageId));
    // when & then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("MES_001"));
    verify(messageService).update(eq(messageId), any(MessageUpdateRequest.class));
  }

  @Test
  @DisplayName("채널 메시지 조회 성공")
  void message_find_all_success_case() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    PageResponse<MessageDto> response = mock(PageResponse.class);
    when(messageService.findAllByChannelId(eq(channelId), isNull(), any(Pageable.class)))
        .thenReturn(response);
    // when & then
    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString()))
        .andExpect(status().isOk());

    ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

    verify(messageService).findAllByChannelId(eq(channelId), isNull(), captor.capture());

    Pageable pageable = captor.getValue();
    assertEquals(0, pageable.getPageNumber());
    assertEquals(50, pageable.getPageSize());
    assertEquals(Sort.Direction.DESC, pageable.getSort().getOrderFor("createdAt").getDirection());
  }

  @Test
  @DisplayName("채널 메시지 조회 실패 - 존재하지 않는 채널")
  void message_find_all_fail_not_found() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    when(messageService.findAllByChannelId(eq(channelId), isNull(), any(Pageable.class)))
        .thenThrow(new ChannelNotFoundException(channelId));
    // when & then
    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId.toString()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("CHANNEL_001"));
    verify(messageService).findAllByChannelId(eq(channelId), isNull(), any(Pageable.class));
  }

  @Test
  @DisplayName("메시지 삭제 성공")
  void message_delete_success_case() throws Exception {
    // given
    UUID messageId = UUID.randomUUID();
    // when & then
    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNoContent())
        .andExpect(content().string(""));
    verify(messageService).delete(messageId);
  }

  @Test
  @DisplayName("메시지 삭제 실패 - 존재하지 않는 메시지")
  void message_delete_fail_not_found() throws Exception {
    // given
    UUID messageId = UUID.randomUUID();
    doThrow(new MessageNotFoundException(messageId))
        .when(messageService).delete(messageId);
    // when & then
    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("MES_001"));
    verify(messageService).delete(messageId);
  }
}
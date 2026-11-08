package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
class MessageControllerTest {
    @Autowired MockMvc mockMvc;
    @MockitoBean MessageService messageService;

    @Test
    @DisplayName("첨부 메시지 생성 성공 시 201과 메시지 JSON 반환")
    void 메시지생성_성공() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        var request = new MockMultipartFile("messageCreateRequest", "", "application/json",
                ("{\"channelId\":\"%s\",\"authorId\":\"%s\",\"content\":\"hello\"}"
                        .formatted(channelId, UUID.randomUUID())).getBytes(StandardCharsets.UTF_8));
        var attachment = new MockMultipartFile("attachments", "photo.png", "image/png", new byte[]{1});
        when(messageService.create(any(MessageCreateRequest.class), anyList())).thenReturn(
                new MessageDto(id, Instant.now(), null, "hello", channelId, null, List.of()));

        // when
        var result = mockMvc.perform(multipart("/api/messages").file(request).file(attachment));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.content").value("hello"));
        verify(messageService).create(any(MessageCreateRequest.class), argThat(files -> files.size() == 1));
    }

    @Test
    @DisplayName("채널 ID가 없으면 400과 검증 오류 반환")
    void 메시지생성_검증실패() throws Exception {
        // given
        var request = new MockMultipartFile("messageCreateRequest", "", "application/json",
                ("{\"authorId\":\"%s\",\"content\":\"hello\"}".formatted(UUID.randomUUID()))
                        .getBytes(StandardCharsets.UTF_8));

        // when
        var result = mockMvc.perform(multipart("/api/messages").file(request));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT.getCode()))
                .andExpect(jsonPath("$.details.channelId").exists());
        verifyNoInteractions(messageService);
    }

    @Test
    @DisplayName("없는 메시지 수정 시 404와 메시지 ID 반환")
    void 메시지수정_실패() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        when(messageService.update(eq(id), any(MessageUpdateRequest.class)))
                .thenThrow(new MessageNotFoundException(id));

        // when
        var result = mockMvc.perform(patch("/api/messages/{id}", id)
                .contentType(MediaType.APPLICATION_JSON).content("{\"newContent\":\"updated\"}"));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.MESSAGE_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.details.messageId").value(id.toString()));
    }

    @Test
    @DisplayName("메시지 커서 페이지 JSON 조회 성공")
    void 메시지조회_성공() throws Exception {
        // given
        UUID channelId = UUID.randomUUID();
        Instant cursor = Instant.parse("2026-01-03T00:00:00Z");
        String nextCursor = "2026-01-02T00:00:00Z";
        when(messageService.findAllByChannelId(eq(channelId), eq(cursor), any(Pageable.class)))
                .thenReturn(new PageResponse<>(List.of(), nextCursor, 2, true, 5L));

        // when
        var result = mockMvc.perform(get("/api/messages").param("channelId", channelId.toString())
                .param("cursor", cursor.toString()).param("size", "2"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.nextCursor").value(nextCursor))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.totalElements").value(5));
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.service.ChannelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChannelController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
class ChannelControllerTest {
    @Autowired MockMvc mockMvc;
    @MockitoBean ChannelService channelService;

    @Test
    @DisplayName("공개 채널 생성 시 201과 채널 JSON 반환")
    void 채널생성_성공() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        when(channelService.create(any(PublicChannelCreateRequest.class))).thenReturn(
                new ChannelDto(id, "general", "description", ChannelType.PUBLIC, List.of(), Instant.now()));

        // when
        var result = mockMvc.perform(post("/api/channels/public").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"general\",\"description\":\"description\"}"));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("general"))
                .andExpect(jsonPath("$.type").value("PUBLIC"));
    }

    @Test
    @DisplayName("빈 채널명은 400과 검증 오류 반환")
    void 채널생성_검증실패() throws Exception {
        // given
        String request = "{\"name\":\"\",\"description\":\"description\"}";

        // when
        var result = mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON).content(request));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT.getCode()))
                .andExpect(jsonPath("$.details.name").exists());
        verifyNoInteractions(channelService);
    }

    @Test
    @DisplayName("없는 채널 수정 시 404와 채널 ID 반환")
    void 채널수정_실패() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        when(channelService.update(eq(id), any(PublicChannelUpdateRequest.class)))
                .thenThrow(new ChannelNotFoundException(id));

        // when
        var result = mockMvc.perform(patch("/api/channels/{id}", id)
                .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"updated\"}"));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.CHANNEL_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.details.channelId").value(id.toString()));
    }

    @Test
    @DisplayName("유저별 채널 목록 JSON 조회 성공")
    void 채널조회_성공() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        when(channelService.findAllByUserId(userId)).thenReturn(List.of(
                new ChannelDto(UUID.randomUUID(), "general", null, ChannelType.PUBLIC, List.of(), Instant.now())));

        // when
        var result = mockMvc.perform(get("/api/channels").param("userId", userId.toString()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("general"));
    }
}

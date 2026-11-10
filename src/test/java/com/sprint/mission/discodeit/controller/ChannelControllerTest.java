package com.sprint.mission.discodeit.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.base.Channel.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

  @Autowired
  MockMvc mockMvc;
  @MockitoBean
  ChannelService channelService;


  @Test
  @DisplayName("PRIVATE 채널 생성 성공")
  void private_channel_create_success() throws Exception {
    // given
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    String json = """
        {
          "participantIds": ["%s", "%s"]
        }
        """.formatted(userId1, userId2);
    ChannelDto channelDto = mock(ChannelDto.class);
    when(channelDto.id()).thenReturn(channelId);
    when(channelService.createPrivate(any(PrivateChannelCreateRequest.class)))
        .thenReturn(channelDto);

    // when & then
    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(channelId.toString()));
    verify(channelService).createPrivate(argThat(request ->
        request.participantIds().equals(List.of(userId1, userId2))));
  }

  @Test
  @DisplayName("PRIVATE 채널 생성 실패 - 참여자 누락")
  void channel_private_create_fail_case() throws Exception {
    // given
    String json = """
        {
          "participantIds": []
        }
        """;
    // when & then
    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(channelService);
  }

  @Test
  @DisplayName("public 채널 생성 성공")
  void public_channel_create_success_case() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    ChannelDto channelDto = new ChannelDto(channelId, ChannelType.PUBLIC, "channelName",
        "channelDesc", List.of(), null);
    String json = """
        {
          "name" : "channelName",
          "description" : "channelDesc"
        }        
        """;
    when(channelService.createPublic(any())).thenReturn(channelDto);
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.type").value("PUBLIC"))
        .andExpect(jsonPath("$.name").value("channelName"))
        .andExpect(jsonPath("$.description").value("channelDesc"));
  }

  @Test
  @DisplayName("public 채널 실패 케이스")
  void channel_public_create_fail_case() throws Exception {
    // given
    String json = """
        {
          "name": "",
          "description":""
        }
        """;
    // when & then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());
    verifyNoInteractions(channelService);
  }

  @Test
  @DisplayName("채널 정보 수정 성공 케이스")
  void channel_update_success_case() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    ChannelDto channelDto = new ChannelDto(
        channelId, ChannelType.PUBLIC, "newName",
        "newDesc", List.of(), null
    );
    String json = """
        {
          "name": "newName",
          "description": "newDesc"
        }
        """;
    when(channelService.update(eq(channelId), any(ChannelUpdateRequest.class)))
        .thenReturn(channelDto);
    // when & then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.name").value("newName"))
        .andExpect(jsonPath("$.description").value("newDesc"));

    verify(channelService).update(eq(channelId), any(ChannelUpdateRequest.class));
  }

  @Test
  @DisplayName("채널 정보 수정 실패 케이스")
  void channel_update_fail_case() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
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
        .andExpect(jsonPath("$.code").value("VALID_EXCEPTION"));
    verifyNoInteractions(channelService);
  }

  @Test
  @DisplayName("채널 삭제 케이스")
  void channel_delete_success_case() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    // when & then
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent())
        .andExpect(content().string(""));
    verify(channelService).delete(channelId);
  }

  @Test
  @DisplayName("채널 삭제 실패 - 존재하지 않는 채널")
  void channel_delete_fail_not_found() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    doThrow(new ChannelNotFoundException(channelId))
        .when(channelService).delete(channelId);
    // when & then
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("CHANNEL_001"));
    verify(channelService).delete(channelId);
  }
}
package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @Tag(name = "PRIVATE 채널 생성")
  @PostMapping("/private")
  public ResponseEntity<ApiResponse<ChannelResponse>> privateCreateChannel(
      @Valid @ModelAttribute PrivateChannelCreateRequest request) {
    ChannelResponse channel = channelService.createPrivate(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(channel));
  }

  @Tag(name = "PUBLIC 채널 생성")
  @PostMapping("/public")
  public ResponseEntity<ApiResponse<ChannelResponse>> publicCreateChannel(
      @Valid @ModelAttribute PublicChannelCreateRequest request) {
    ChannelResponse channel = channelService.createPublic(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(channel));
  }

/*  @Tag(name = "PUBLIC 채널 유저 추가")
  @PostMapping("/public/user")
  public ResponseEntity<ApiResponse<ChannelResponse>> publicChannelAddUser(
      @Valid @RequestParam("channel-id") UUID channelId, @RequestParam("user-id") UUID userId) {
    ChannelResponse channel = channelService.addUserToChannel(channelId, userId);
    return ResponseEntity.ok().body(ApiResponse.success(channel));
  }*/

  @Tag(name = "채널 정보 호출")
  @GetMapping
  public ResponseEntity<ApiResponse<List<ChannelResponse>>> allPublicChannel(
      @Valid @RequestParam(required = false) UUID userId) {
    List<ChannelResponse> channelList = channelService.findAllByUserId(userId);
    return ResponseEntity.ok().body(ApiResponse.success(channelList));
  }

  @Tag(name = "PUBLIC 채널 정보 수정")
  @PatchMapping("/{channelId}")
  public ResponseEntity<ApiResponse<ChannelResponse>> updatePublicChannel(
      @PathVariable("channelId") UUID channelId,
      @Valid @ModelAttribute ChannelUpdateRequest request) {
    ChannelResponse channelResponse = channelService.update(channelId, request);
    return ResponseEntity.ok().body(ApiResponse.success(channelResponse));
  }

/*  @Tag(name = "특정 유저 전체 채널")
  @GetMapping
  public ResponseEntity<ApiResponse<List<ChannelResponse>>> findAllByUserId(
      @RequestParam("userId") UUID userId) {
    List<ChannelResponse> channelList = channelService.findAllByUserId(userId);
    return ResponseEntity.ok().body(ApiResponse.success(channelList));
  }*/

  @Tag(name = "채널 삭제")
  @DeleteMapping("/{channelId}")
  public ResponseEntity<ApiResponse<Void>> deleteChannel(
      @PathVariable("channelId") UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }


}

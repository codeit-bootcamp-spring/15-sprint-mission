package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
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
  public ResponseEntity<ChannelDto> privateCreateChannel(
      @Valid @RequestBody PrivateChannelCreateRequest request) {
    ChannelDto channel = channelService.createPrivate(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(channel);
  }

  @Tag(name = "PUBLIC 채널 생성")
  @PostMapping("/public")
  public ResponseEntity<ChannelDto> publicCreateChannel(
      @Valid @RequestBody PublicChannelCreateRequest request) {
    ChannelDto channel = channelService.createPublic(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(channel);
  }

/*  @Tag(name = "PUBLIC 채널 유저 추가")
  @PostMapping("/public/user")
  public ResponseEntity<ChannelResponse> publicChannelAddUser(
      @Valid @RequestParam("channel-id") UUID channelId, @RequestParam("user-id") UUID userId) {
    ChannelResponse channel = channelService.addUserToChannel(channelId, userId);
    return ResponseEntity.ok().body(channel);
  }*/

  @Tag(name = "채널 정보 호출")
  @GetMapping
  public ResponseEntity<List<ChannelDto>> allPublicChannel(
      @Valid @RequestParam(required = false) UUID userId) {
    List<ChannelDto> channelList = channelService.findAllByUserId(userId);
    return ResponseEntity.ok().body(channelList);
  }

  @Tag(name = "PUBLIC 채널 정보 수정")
  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelDto> updatePublicChannel(
      @PathVariable("channelId") UUID channelId,
      @Valid @ModelAttribute ChannelUpdateRequest request) {
    ChannelDto channelResponse = channelService.update(channelId, request);
    return ResponseEntity.ok().body(channelResponse);
  }

/*  @Tag(name = "특정 유저 전체 채널")
  @GetMapping
  public ResponseEntity<List<ChannelResponse>> findAllByUserId(
      @RequestParam("userId") UUID userId) {
    List<ChannelResponse> channelList = channelService.findAllByUserId(userId);
    return ResponseEntity.ok().body(channelList);
  }*/

  @Tag(name = "채널 삭제")
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(
      @PathVariable("channelId") UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }


}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
@Slf4j
public class ChannelController {

  private final ChannelService channelService;

  @Tag(name = "PRIVATE 채널 생성")
  @PostMapping("/private")
  public ResponseEntity<ChannelDto> privateCreateChannel(
      @Valid @RequestBody PrivateChannelCreateRequest request) {
    log.debug("Private channel 생성 API 요청 : userIds = {} ", request.participantIds());
    ChannelDto channel = channelService.createPrivate(request);
    log.debug("Private channel 생성 API 요청 완료 : channelId = {} ", channel.id());
    return ResponseEntity.status(HttpStatus.CREATED).body(channel);
  }

  @Tag(name = "PUBLIC 채널 생성")
  @PostMapping("/public")
  public ResponseEntity<ChannelDto> publicCreateChannel(
      @Valid @RequestBody PublicChannelCreateRequest request) {
    log.debug("Public Channel 생성 API 요청 : channelName = {}, ChannelDescription = {}",
        request.name(), request.description());
    ChannelDto channel = channelService.createPublic(request);
    log.debug("Public Channel 생성 API 요청 완료 : channelId = {}", channel.id());
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
    log.debug("채널 정보 호출 API 요청 : userId = {}", userId);
    List<ChannelDto> channelList = channelService.findAllByUserId(userId);
    log.debug("채널 정보 호출 API 요청 완료 : userId = {}, channelIds = {}", userId,
        channelList.stream().map(ChannelDto::id).toList());
    return ResponseEntity.ok().body(channelList);
  }

  @Tag(name = "PUBLIC 채널 정보 수정")
  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelDto> updatePublicChannel(
      @PathVariable("channelId") UUID channelId,
      @Valid @RequestBody ChannelUpdateRequest request) {
    log.debug("채널 정보 수정 API 요청 : channelId = {}", channelId);
    ChannelDto channelResponse = channelService.update(channelId, request);
    log.debug("채널 정보 수정 API 요청 완료 : channelId = {}, channelNewName = {}, channelNewDesc = {}",
        channelResponse.id(), channelResponse.name(), channelResponse.description());
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
    log.debug("채널 삭제 API 요청 : channelId = {}", channelId);
    channelService.delete(channelId);
    log.debug("채널 삭제 API 요청 완료 : channelId = {}", channelId);
    return ResponseEntity.noContent().build();
  }


}

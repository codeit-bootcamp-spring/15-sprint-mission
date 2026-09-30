package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateDto;
import com.sprint.mission.discodeit.dto.PublicChannelCreateDto;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Channel", description = "채널 관련 API")
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @Operation(summary = "공개 채널 생성", description = "새로운 공개 채널을 생성합니다.")
  @PostMapping("/public")
  public ResponseEntity<ChannelResponseDto> createPublicChannel(
      @RequestBody PublicChannelCreateDto dto) {
    ChannelResponseDto response = channelService.createPublicChannel(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "비공개 채널 생성", description = "새로운 비공개 채널을 생성합니다.")
  @PostMapping("/private")
  public ResponseEntity<ChannelResponseDto> createPrivateChannel(
      @RequestBody PrivateChannelCreateDto dto) {
    ChannelResponseDto response = channelService.createPrivateChannel(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "채널 단건 조회", description = "채널 ID로 상세 정보를 조회합니다.")
  @GetMapping("/{id}")
  public ResponseEntity<ChannelResponseDto> find(@PathVariable("id") UUID id) {
    ChannelResponseDto response = channelService.find(id);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "사용자별 채널 목록 조회", description = "특정 사용자가 참여/접근 가능한 채널 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<List<ChannelResponseDto>> findAllByUserId(
      @RequestParam("userId") UUID userId) {
    List<ChannelResponseDto> response = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "공개 채널 정보 수정", description = "채널 이름이나 설명을 부분 수정합니다.")
  @PatchMapping("/{id}")
  public ResponseEntity<ChannelResponseDto> update(
      @PathVariable("id") UUID id,
      @RequestBody ChannelUpdateDto dto) {
    ChannelResponseDto response = channelService.update(id, dto);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "채널 삭제", description = "채널 ID를 통해 채널을 삭제합니다.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
    channelService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
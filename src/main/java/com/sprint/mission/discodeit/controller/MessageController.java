package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageCreateDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
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

@Tag(name = "Message", description = "메시지 관련 API")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @Operation(summary = "메시지 생성(전송)", description = "채널에 새로운 메시지를 전송합니다.")
  @PostMapping
  public ResponseEntity<Message> create(@RequestBody MessageCreateDto dto) {
    Message response = messageService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "채널별 메시지 목록 조회", description = "특정 채널에 속한 메시지 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<List<Message>> findAllByChannelId(
      @RequestParam("channelId") UUID channelId) {
    List<Message> response = messageService.findAllByChannelId(channelId);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "메시지 단건 조회", description = "메시지 ID로 단건 메시지를 조회합니다.")
  @GetMapping("/{id}")
  public ResponseEntity<Message> find(@PathVariable("id") UUID id) {
    Message response = messageService.find(id);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "메시지 수정", description = "메시지 내용을 수정합니다.")
  @PatchMapping("/{id}")
  public ResponseEntity<Message> update(
      @PathVariable("id") UUID id,
      @RequestBody MessageUpdateDto dto) {
    Message response = messageService.update(id, dto);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "메시지 삭제", description = "메시지 ID로 메시지를 삭제합니다.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
    messageService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
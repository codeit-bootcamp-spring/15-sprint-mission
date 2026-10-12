package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.service.ReadStatusService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Tag(name = "ReadStatus", description = "메시지 수신/읽음 상태 관련 API")
@RestController
@RequestMapping({"/api/readStatuses", "/api/read-statuses"})
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;
  private final ReadStatusMapper readStatusMapper;

  @Operation(summary = "메시지 수신 정보 생성/갱신", description = "특정 채널의 메시지 수신/읽음 상태를 생성하거나 갱신합니다.")
  @PostMapping
  public ResponseEntity<ReadStatusDto> create(@RequestBody ReadStatusCreateRequest request) {
    var entity = readStatusService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatusMapper.toDto(entity));
  }

  @Operation(summary = "메시지 수신 정보 단건 조회", description = "ID를 통해 메시지 수신 정보를 조회합니다.")
  @GetMapping("/{id}")
  public ResponseEntity<ReadStatusDto> find(@PathVariable("id") UUID id) {
    var entity = readStatusService.find(id);
    return ResponseEntity.ok(readStatusMapper.toDto(entity));
  }

  @Operation(summary = "사용자별 메시지 수신 정보 목록 조회", description = "특정 사용자의 메시지 수신 정보 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(@RequestParam("userId") UUID userId) {
    var list = readStatusService.findAllByUserId(userId);
    // null이거나 비어있으면 안전하게 빈 배열 [] 반환
    if (list == null || list.isEmpty()) {
      return ResponseEntity.ok(Collections.emptyList());
    }
    var dtos = list.stream().map(readStatusMapper::toDto).toList();
    return ResponseEntity.ok(dtos);
  }

  @Operation(summary = "메시지 수신 정보 수정 (PATCH)", description = "마지막으로 읽은 시각 등 수신 상태를 부분 수정합니다.")
  @PatchMapping("/{id}")
  public ResponseEntity<ReadStatusDto> update(
      @PathVariable("id") UUID id,
      @RequestBody ReadStatusUpdateRequest request) {
    var entity = readStatusService.update(id, request);
    return ResponseEntity.ok(readStatusMapper.toDto(entity));
  }

  @Operation(summary = "메시지 수신 정보 수정 (PUT)", description = "마지막으로 읽은 시각 등 수신 상태를 갱신합니다.")
  @PutMapping("/{id}")
  public ResponseEntity<ReadStatusDto> updatePut(
      @PathVariable("id") UUID id,
      @RequestBody ReadStatusUpdateRequest request) {
    var entity = readStatusService.update(id, request);
    return ResponseEntity.ok(readStatusMapper.toDto(entity));
  }

  @Operation(summary = "메시지 수신 정보 삭제", description = "메시지 수신 정보를 삭제합니다.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
    readStatusService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
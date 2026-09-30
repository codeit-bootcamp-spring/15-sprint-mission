package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "ReadStatus", description = "메시지 수신/읽음 상태 관련 API")
@RestController
@RequestMapping("/api/read-statuses")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @Operation(summary = "메시지 수신 정보 생성", description = "특정 채널의 메시지 수신/읽음 상태를 생성합니다.")
  @PostMapping
  public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusCreateRequest request) {
    ReadStatus response = readStatusService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "메시지 수신 정보 단건 조회", description = "ID를 통해 메시지 수신 정보를 조회합니다.")
  @GetMapping("/{id}")
  public ResponseEntity<ReadStatus> find(@PathVariable("id") UUID id) {
    ReadStatus response = readStatusService.find(id);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "사용자별 메시지 수신 정보 목록 조회", description = "특정 사용자의 메시지 수신 정보 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<List<ReadStatus>> findAllByUserId(@RequestParam("userId") UUID userId) {
    List<ReadStatus> response = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "메시지 수신 정보 수정", description = "마지막으로 읽은 시각 등 수신 상태를 부분 수정합니다.")
  @PatchMapping("/{id}")
  public ResponseEntity<ReadStatus> update(
      @PathVariable("id") UUID id,
      @RequestBody ReadStatusUpdateRequest request) {
    ReadStatus response = readStatusService.update(id, request);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "메시지 수신 정보 삭제", description = "메시지 수신 정보를 삭제합니다.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
    readStatusService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
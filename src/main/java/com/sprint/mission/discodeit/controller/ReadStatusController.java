package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.desktop.UserSessionEvent;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/read-status")
public class ReadStatusController {
    // 메시지 수신 정보 관리
    private final ReadStatusService readStatusService;

    public ReadStatusController(ReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }

    // 메시지 수신정보 생성
    @PostMapping
    public ResponseEntity<ReadStatusResponse> createReadStatus(
            @Valid @RequestBody ReadStatusCreateRequest request) {
        ReadStatus readStatus = readStatusService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ReadStatusResponse.from(readStatus));
    }

    // 메시지 수신정보 수정
    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusResponse> updateReadStatus(
            @PathVariable UUID readStatusId,
            @Valid @RequestBody ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusService.update(readStatusId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ReadStatusResponse.from(readStatus));
    }

    // 메시지 수신 정보 조회
    @GetMapping("user/{userId}")
    public ResponseEntity<List<ReadStatusResponse>> getReadStatusByUserId(
            @PathVariable UUID userId) {
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
        List<ReadStatusResponse> responses = readStatuses.stream()
                .map(ReadStatusResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}

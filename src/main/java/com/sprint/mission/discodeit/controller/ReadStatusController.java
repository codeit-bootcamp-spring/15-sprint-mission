package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
//import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.desktop.UserSessionEvent;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatus")
public class ReadStatusController {
    // 메시지 수신 정보 관리
    private final ReadStatusService readStatusService;

    // 메시지 수신정보 생성
    @PostMapping
    public ResponseEntity<ReadStatus> createReadStatus(@RequestBody ReadStatusCreateRequest request) {
        ReadStatus createdReadStatus = readStatusService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdReadStatus);
    }

    // 메시지 수신정보 수정
    @PatchMapping(path = "{readStatusId}")
    public ResponseEntity<ReadStatus> update(@PathVariable("readStatusId") UUID readStatusId,
            @RequestBody ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusService.update(readStatusId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(readStatus);
    }

    // 메시지 수신 정보 조회
    @GetMapping
    public ResponseEntity<List<ReadStatus>> findAllByUserId(@RequestParam("userId") UUID userId) {
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(readStatuses);
    }
}

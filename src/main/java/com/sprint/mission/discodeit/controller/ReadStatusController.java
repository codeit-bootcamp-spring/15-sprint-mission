package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-statuses")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    // 1. 특정 채널의 메시지 수신 정보 생성 (POST /api/read-statuses)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusCreateRequest request) {
        ReadStatus response = readStatusService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. 수신 정보 단건 조회 (GET /api/read-statuses/{id})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ReadStatus> find(@PathVariable("id") UUID id) {
        ReadStatus response = readStatusService.find(id);
        return ResponseEntity.ok(response);
    }

    // 3. 특정 사용자의 메시지 수신 정보 목록 조회 (GET /api/read-statuses?userId={userId})
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatus>> findAllByUserId(@RequestParam("userId") UUID userId) {
        List<ReadStatus> response = readStatusService.findAllByUserId(userId);
        return ResponseEntity.ok(response);
    }

    // 4. 특정 채널의 메시지 수신 정보 수정 (PATCH /api/read-statuses/{id})
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<ReadStatus> update(
            @PathVariable("id") UUID id,
            @RequestBody ReadStatusUpdateRequest request) {
        // ReadStatusUpdateRequest에 id 설정이 필요하다면 주입 (예: request.setId(id))
        ReadStatus response = readStatusService.update(request);
        return ResponseEntity.ok(response);
    }

    // 5. 메시지 수신 정보 삭제 (DELETE /api/read-statuses/{id})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        readStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
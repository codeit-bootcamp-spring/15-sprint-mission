package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
import jakarta.validation.Valid;
import lombok.Locked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ReadStatusController {
    private final BasicReadStatusService readStatusService;

    public ReadStatusController(BasicReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }

    // 생성
    @RequestMapping(value="/v1/channels/{channel-id}/read-status", method= RequestMethod.POST)
    public ResponseEntity<ApiResponse<ReadStatusResponse>> createReadStatus(
            @Valid @ModelAttribute ReadStatusCreateRequest request,
            @PathVariable("channel-id") UUID channelId) {
        ReadStatus readStatus = readStatusService.create(request, channelId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(ReadStatusResponse.from(readStatus)));
    }

    // 조회
    @RequestMapping(value="/v1/users/{user-id}/read-status", method= RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<ReadStatusResponse>>> getReadStatus(
            @PathVariable("user-id") UUID userId) {
        List<ReadStatusResponse> readStatus = readStatusService.findAllByUserId(userId)
                .stream().map(ReadStatusResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.success(readStatus));
    }

    // 수정
    @RequestMapping(value="/v1/channels/{channel-id}/read-status", method= RequestMethod.PATCH)
    public ResponseEntity<ApiResponse<ReadStatusResponse>> updateReadStatus(
            @Valid @ModelAttribute ReadStatusUpdateRequest request,
            @PathVariable("channel-id") UUID channelId,
            @RequestParam UUID readStatusId
    ) {
        ReadStatus readStatus = readStatusService.update(readStatusId, channelId, request);
        return ResponseEntity.ok(ApiResponse.success(ReadStatusResponse.from(readStatus)));
    }
}

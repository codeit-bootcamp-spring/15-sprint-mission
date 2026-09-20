package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.global.ApiResponse;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/read-status")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReadStatus>> createReadStatus(@RequestBody ReadStatusCreateRequest readStatusCreateRequest) {
        ReadStatus readStatus = readStatusService.create(readStatusCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(readStatus));
    }

    @PatchMapping("/by-user-id/{user-id}")
    public ResponseEntity<ApiResponse<List<ReadStatus>>> patchReadStatusByUserId(@PathVariable("user-id") UUID userId){
        List<ReadStatus> readStatusList = readStatusService.updateAllByUserId(userId);

        return ResponseEntity.ok(ApiResponse.success(readStatusList));
    }

    @PatchMapping("/by-channel-id/{channel-id}")
    public ResponseEntity<ApiResponse<List<ReadStatus>>> patchReadStatusByChannelId(@PathVariable("channel-id") UUID channelId){
        List<ReadStatus> readStatusList = readStatusService.updateAllByChannelId(channelId);

        return ResponseEntity.ok(ApiResponse.success(readStatusList));
    }




}

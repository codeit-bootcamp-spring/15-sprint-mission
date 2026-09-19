package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/read")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @Tag(name = "상태 추가")
    @PostMapping
    public ResponseEntity<ApiResponse<ReadStatus>> create(@Valid @RequestBody ReadStatusCreateRequest request) {
        ReadStatus readStatus = readStatusService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(readStatus));
    }

    @Tag(name = "특정 유저 채널별 읽음 확인")
    @GetMapping("/user/{user-id}")
    public ResponseEntity<ApiResponse<List<ReadStatus>>> getStatusByUser(@PathVariable("user-id")UUID userId){
        List<ReadStatus> list = readStatusService.findAllByUserId(userId);
        return ResponseEntity.ok().body(ApiResponse.success(list));
    }

    @Tag(name = "특정 채널 전체 읽음 확인")
    @GetMapping("/channel/{channel-id}")
    public ResponseEntity<ApiResponse<List<ReadStatus>>> getStatusByChannel(@PathVariable("channel-id")UUID channelId){
        List<ReadStatus> list = readStatusService.findAllByChannelId(channelId);
        return ResponseEntity.ok().body(ApiResponse.success(list));
    }

}

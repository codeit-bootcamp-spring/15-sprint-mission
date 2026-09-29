package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController {

    private final ReadStatusService readStatusService;


    @Operation(
            summary = "Message 읽음 상태 생성",
            operationId = "create_1"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "ReadStatus 등록 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "리드스테이터스 이미 존재함"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "채널 또는 유저를 찾을 수 없음"
            )
    })
    @PostMapping
    public ResponseEntity<ReadStatus> createReadStatus(@RequestBody ReadStatusCreateRequest readStatusCreateRequest) {
        ReadStatus readStatus = readStatusService.create(readStatusCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(readStatus);
    }

    @Operation(
            summary = "Message 읽음 상태 수정",
            operationId = "update_1"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "리드스테이터스 수정 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "리드스테이터스 찾을 수 없음"
            )
    })
    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatus> patchReadStatus(@PathVariable("readStatusId") UUID readStatusId){
        ReadStatus readStatus = readStatusService.update(readStatusId);

        return ResponseEntity.ok(readStatus);
    }


    /*@PatchMapping("/by-user-id/{user-id}")
    public ResponseEntity<List<ReadStatus>> patchReadStatusByUserId(@PathVariable("user-id") UUID userId){
        List<ReadStatus> readStatusList = readStatusService.updateAllByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(readStatusList);
    }

    @PatchMapping("/by-channel-id/{channel-id}")
    public ResponseEntity<List<ReadStatus>> patchReadStatusByChannelId(@PathVariable("channel-id") UUID channelId){
        List<ReadStatus> readStatusList = readStatusService.updateAllByChannelId(channelId);

        return ResponseEntity.status(HttpStatus.OK).body(readStatusList);
    }*/

    @Operation(
            summary = "User의 Message 읽음 상태 목록 조회",
            operationId = "findAllByUserId"
    )
    @ApiResponse(
            responseCode = "200",
            description = "유저의 리드스테이터스 목록 조회 성공"
    )
    @GetMapping
    public ResponseEntity<List<ReadStatus>> getReadStatusByUserId( @RequestParam("userId") UUID userId){
        List<ReadStatus> readStatusList = readStatusService.findAllByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(readStatusList);
    }





}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Tag(name="ReadStatus", description = "Message 읽음 상태 API")
@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController {
    private final BasicReadStatusService readStatusService;

    public ReadStatusController(BasicReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }

    // 생성
    @Operation(summary = "Message 읽음 상태 생성", operationId = "create_1")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이미 읽음 상태가 존재함")
    })
    @RequestMapping(method= RequestMethod.POST)
    public ResponseEntity<ApiResponse<ReadStatusResponse>> createReadStatus(
            @Valid @RequestBody ReadStatusCreateRequest request) {
        ReadStatus readStatus = readStatusService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(ReadStatusResponse.from(readStatus)));
    }

    // 조회
    @Operation(summary = "User의 Message 읽음 상태 목록 조회", operationId = "findAllByUserId")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Message 읽음 상태 목록 조회 성공")
    })
    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<ReadStatusResponse>>> getReadStatus(
            @PathVariable("user-id") UUID userId) {
        List<ReadStatusResponse> readStatus = readStatusService.findAllByUserId(userId)
                .stream().map(ReadStatusResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.success(readStatus));
    }

    // 수정
    @Operation(summary = "Message 읽음 상태 수정", operationId = "update_1")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Message 읽음 상태가 성공적으로 수정됨"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Message 읽음 상태를 찾을 수 없음"),
    })
    @RequestMapping(value="/{readstatus-id}", method= RequestMethod.PATCH)
    public ResponseEntity<ApiResponse<ReadStatusResponse>> updateReadStatus(
            @RequestPart("readStatusUpdateRequest") ReadStatusUpdateRequest request,
            @RequestParam("readstatus-id") UUID readStatusId
    ) {

        ReadStatus readStatus = readStatusService.update(readStatusId, request);
        return ResponseEntity.ok(ApiResponse.success(ReadStatusResponse.from(readStatus)));
    }
}

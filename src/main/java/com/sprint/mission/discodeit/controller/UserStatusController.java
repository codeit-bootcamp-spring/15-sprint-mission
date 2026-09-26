package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;

import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")

public class UserStatusController {
    private final BasicUserStatusService userStatusService;

    public UserStatusController(BasicUserStatusService userStatusService) {
        this.userStatusService = userStatusService;
    }

    @Operation(summary = "User 온라인 상태 업데이트", operationId = "updateUserStatusByUserId")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트됨")
    })
    @RequestMapping(value="/{userId}/userStatus" , method= RequestMethod.PATCH)
    public ResponseEntity<UserStatusResponse> updateUserStatus(
            @PathVariable("userId") UUID userId,
            @Valid @RequestBody UserStatusUpdateRequest request
    ) {
       UserStatus status = userStatusService.updateByUserId(userId, request);
       return ResponseEntity.ok(UserStatusResponse.from(status));
    }



}

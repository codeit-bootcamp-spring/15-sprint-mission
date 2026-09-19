package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
public class UserStatusController {
    private final BasicUserStatusService userStatusService;

    public UserStatusController(BasicUserStatusService userStatusService) {
        this.userStatusService = userStatusService;
    }
    @RequestMapping(value="/{user-id}/online" , method= RequestMethod.PATCH)
    public ResponseEntity<ApiResponse<UserStatusResponse>> updateUserStatus(
            @RequestParam UUID userId,
            @Valid @ModelAttribute UserStatusUpdateRequest request
    ) {
       UserStatus status = userStatusService.update(userId, request);
       return ResponseEntity.ok(ApiResponse.success(UserStatusResponse.from(status)));
    }



}

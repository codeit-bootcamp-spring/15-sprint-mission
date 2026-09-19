package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.ApiResponse;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/user-status")
public class UserStatusController {
    private final UserStatusService userStatusService;

    @PutMapping("/by-user-id/{user-id}")
    public ResponseEntity<ApiResponse<UserStatus>> updateUserStatusByUserId(@PathVariable("user-id") UUID userId){
        UserStatus userStatus=userStatusService.updateByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(userStatus));

    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserStatus>> updateUserStatus(@PathVariable UUID id){
        UserStatus userStatus=userStatusService.update(id);
        return ResponseEntity.ok(ApiResponse.success(userStatus));
    }

}

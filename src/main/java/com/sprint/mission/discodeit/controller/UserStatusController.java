package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/userStatus")
public class UserStatusController {
    private final UserStatusService userStatusService;

    @PutMapping("/by-user-id/{user-id}")
    public ResponseEntity<UserStatus> updateUserStatusByUserId(@PathVariable("user-id") UUID userId){
        UserStatus userStatus=userStatusService.updateByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userStatus);

    }
    @PutMapping("/{id}")
    public ResponseEntity<UserStatus> updateUserStatus(@PathVariable UUID id){
        UserStatus userStatus=userStatusService.update(id);
        return ResponseEntity.status(HttpStatus.OK).body(userStatus);
    }

}

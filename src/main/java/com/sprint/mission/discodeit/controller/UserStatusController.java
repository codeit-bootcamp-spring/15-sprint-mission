package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-status")
public class UserStatusController {

  private final UserStatusService userStatusService;


}

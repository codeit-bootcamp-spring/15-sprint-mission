package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-status")
public class UserStatusController {

  private final UserStatusService userStatusService;


}

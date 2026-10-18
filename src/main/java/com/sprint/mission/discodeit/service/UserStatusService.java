package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.UserStatusDto;

import java.util.UUID;

public interface UserStatusService {
    UserStatusDto create(UserStatusCreateRequest userStatusCreateRequest);
    UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest);

}

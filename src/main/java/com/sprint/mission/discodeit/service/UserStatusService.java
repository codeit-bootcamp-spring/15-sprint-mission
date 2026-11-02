package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserStatusDto;

import java.util.UUID;

public interface UserStatusService {
    //UserStatusDto create(UserStatusCreateRequest userStatusCreateRequest);
    UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest);

}

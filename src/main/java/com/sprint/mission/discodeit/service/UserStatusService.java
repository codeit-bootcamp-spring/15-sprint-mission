package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusDto create(UserStatusCreateRequest userStatusCreateRequest);
    //UserStatus find(UUID id);
    //List<UserStatus> findAll();
    //UserStatus update(UUID id, UserStatusUpdateRequest userStatusUpdateRequest);
    UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest);
    //void delete(UUID id);

}

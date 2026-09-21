package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

    UserStatusDto create(UserStatusCreateRequest request);

    UserStatusDto find(UUID id);

    List<UserStatusDto> findAll();

    UserStatusDto update(UserStatusUpdateRequest request);

    UserStatusDto updateByUserId(UUID userId, Instant lastActiveAt);

    void delete(UUID id);
}


package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateRequest rcr);
    ReadStatus find(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    void update(ReadStatusUpdateRequest rur);
    void delete(UUID id);
}

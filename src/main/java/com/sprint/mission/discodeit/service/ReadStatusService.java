package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateRequest readStatusCreateRequest);
    ReadStatus find(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus update(UUID id);
    void delete(UUID id);

}

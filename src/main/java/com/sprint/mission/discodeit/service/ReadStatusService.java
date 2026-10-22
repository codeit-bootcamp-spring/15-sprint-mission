package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.Request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.ReadStatusDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusDto create(ReadStatusCreateRequest readStatusCreateRequest);
    List<ReadStatusDto> findAllByUserId(UUID userId);
    ReadStatusDto update(UUID id, ReadStatusUpdateRequest readStatusUpdateRequest);

}

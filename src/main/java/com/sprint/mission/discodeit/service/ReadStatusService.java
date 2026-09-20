package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateRequest readStatusCreateRequest);
    ReadStatus find(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    List<ReadStatus> findAllByChannelId(UUID channelId);
    ReadStatus update(UUID id);
    List<ReadStatus> updateAllByUserId(UUID userId);
    List<ReadStatus> updateAllByChannelId(UUID channelId);
    void delete(UUID id);

}

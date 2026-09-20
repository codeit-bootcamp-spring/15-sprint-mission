package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {
    boolean save(ReadStatus readStatus);
    ReadStatus isAlreadyExist(UUID userId, UUID channelId);
    ReadStatus find(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    List<ReadStatus> findAllByChannelId(UUID channelId);
    void delete(UUID id);
}

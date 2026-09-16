package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {

    ReadStatus save(ReadStatus readStatus);
    ReadStatus read(UUID id);
    List<ReadStatus> readAllByUserId(UUID userId);
    List<ReadStatus> readAllByChannelId(UUID channelId);
    void delete(UUID id);
    void deleteAllByChannelId(UUID channelId);
    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);
}

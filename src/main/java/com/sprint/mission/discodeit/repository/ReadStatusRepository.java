package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {

    ReadStatus save(ReadStatus readStatus);

    List<ReadStatus> findAll();

    void deleteByChannelId(UUID channelId);

    List<ReadStatus> findAllByChannelId(UUID channelId);

    Optional<ReadStatus> findById(UUID id);

    Optional<ReadStatus> findByChannelIdAndUserId(
            UUID channelId,
            UUID userId
    );

    List<ReadStatus> findAllByUserId(UUID userId);

    void deleteById(UUID id);
}
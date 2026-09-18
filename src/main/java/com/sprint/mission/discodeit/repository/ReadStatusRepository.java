package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// 공통 Repository가 있다면 extends Repository<ReadStatus, UUID> 추가 가능
public interface ReadStatusRepository {

    ReadStatus save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    boolean existsById(UUID id);
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);
    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);
    List<ReadStatus> findAllByUserId(UUID userId);
    List<ReadStatus> findAllByChannelId(UUID channelId);
    void deleteById(UUID id);
    void deleteAllByChannelId(UUID channelId);
}
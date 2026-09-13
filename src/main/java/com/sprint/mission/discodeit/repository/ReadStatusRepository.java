package com.sprint.mission.discodeit.repository;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface ReadStatusRepository {
    ReadStatus save(ReadStatus entity);
    Optional<ReadStatus> findById(UUID id);
    List<ReadStatus> findAll();
    void deleteById(UUID id);
    boolean existsById(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    List<ReadStatus> findAllByChannelId(UUID channelId);
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);
}

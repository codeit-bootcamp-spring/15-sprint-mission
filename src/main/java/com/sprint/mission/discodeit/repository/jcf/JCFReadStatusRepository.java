package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true
)
public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> readStatuses
            = new ConcurrentHashMap<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatuses.put(
                readStatus.getId(),
                readStatus
        );

        return readStatus;
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(readStatuses.values());
    }

    @Override
    public void deleteByChannelId(UUID channelId) {

        readStatuses.values().removeIf(
                readStatus ->
                        readStatus.getChannelId().equals(channelId)
        );
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {

        List<ReadStatus> result = new ArrayList<>();

        for (ReadStatus readStatus : readStatuses.values()) {

            if (readStatus.getChannelId().equals(channelId)) {
                result.add(readStatus);
            }
        }

        return result;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {

        return Optional.ofNullable(
                readStatuses.get(id)
        );
    }

    @Override
    public Optional<ReadStatus> findByChannelIdAndUserId(
            UUID channelId,
            UUID userId
    ) {

        for (ReadStatus readStatus : readStatuses.values()) {

            if (
                    readStatus.getChannelId().equals(channelId)
                            && readStatus.getUserId().equals(userId)
            ) {
                return Optional.of(readStatus);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {

        List<ReadStatus> result = new ArrayList<>();

        for (ReadStatus readStatus : readStatuses.values()) {

            if (readStatus.getUserId().equals(userId)) {
                result.add(readStatus);
            }
        }

        return result;
    }

    @Override
    public void deleteById(UUID id) {
        readStatuses.remove(id);
    }
}
package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> data = new HashMap<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return data.values().stream()
                .filter(s -> s.getUserId().equals(userId) && s.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return data.values().stream()
                .anyMatch(s -> s.getUserId().equals(userId) && s.getChannelId().equals(channelId));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(s -> s.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(s -> s.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        data.values().removeIf(s -> s.getChannelId().equals(channelId));
    }
}
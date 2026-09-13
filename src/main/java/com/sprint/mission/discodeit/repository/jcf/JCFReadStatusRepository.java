package com.sprint.mission.discodeit.repository.jcf;
import org.springframework.stereotype.Repository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.*;
@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> data = new HashMap<>();
    @Override
    public ReadStatus save(ReadStatus entity) {
        data.put(entity.getId(), entity);
        return entity;
    }
    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }
    @Override
    public List<ReadStatus> findAll() {
        return List.copyOf(data.values());
    }
    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
    public List<ReadStatus> findAllByUserId(UUID userId) {
        List<ReadStatus> result = new ArrayList<>();
        for (ReadStatus status : findAll()) {
            if (status.getUserId().equals(userId)) {
                result.add(status);
            }
        }
        return result;
    }
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        List<ReadStatus> result = new ArrayList<>();
        for (ReadStatus status : findAll()) {
            if (status.getChannelId().equals(channelId)) {
                result.add(status);
            }
        }
        return result;
    }
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        for (ReadStatus status : findAll()) {
            if (status.getUserId().equals(userId) && status.getChannelId().equals(channelId)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}

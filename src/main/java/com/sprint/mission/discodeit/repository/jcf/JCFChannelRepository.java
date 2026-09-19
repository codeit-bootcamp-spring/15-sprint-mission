package com.sprint.mission.discodeit.repository.jcf;
import org.springframework.stereotype.Repository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.*;
@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data = new HashMap<>();
    @Override
    public Channel save(Channel entity) {
        data.put(entity.getId(), entity);
        return entity;
    }
    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }
    @Override
    public List<Channel> findAll() {
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

    @Override
    public boolean existsUserByChannelId(UUID channelId, UUID userId) {
        return false;
    }

}

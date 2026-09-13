package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data;
    private final ReadStatusRepository readStatusRepository;

    public JCFChannelRepository(ReadStatusRepository readStatusRepository) {
        this.data = new HashMap<>();
        this.readStatusRepository = readStatusRepository;
    }

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel read(UUID channelId) {
        return data.get(channelId);
    }

    @Override
    public List<Channel> readAll() {
        return data.values().stream().toList();
    }

    @Override
    public List<Channel> readAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(channel -> {
                    if (channel.getChannelType() == ChannelType.PUBLIC) {
                        return true;
                    }
                    return readStatusRepository.readAllByChannelId(channel.getId()).stream()
                            .anyMatch(readStatus -> readStatus.getUserId().equals(userId));
                })
                .toList();
    }

    @Override
    public void delete(UUID channelId) {
        data.remove(channelId);
    }
}
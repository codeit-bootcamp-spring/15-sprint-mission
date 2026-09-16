package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf", matchIfMissing = true)
public class JCFChannelRepository implements ChannelRepository {
    private final List<Channel> channels;

    public JCFChannelRepository() {
        this.channels = new ArrayList<>();
    }


    @Override
    public boolean create(Channel channel) {
        return this.channels.add(channel);
    }

    @Override
    public Channel find(UUID id) {
        return this.channels.stream().filter(x-> x.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Channel findByChannelName(String channelName) {
        return this.channels.stream().filter(x-> x.getName().equals(channelName)).findFirst().orElse(null);
    }

    @Override
    public List<Channel> findAll() {
        return this.channels.stream().toList();
    }

    @Override
    public boolean update(Channel channel) {
        boolean isExist = this.channels.removeIf(x-> x.getId().equals(channel.getId()));
        if (!isExist) return false;

        this.channels.add(channel);
        return true;
    }

    @Override
    public boolean delete(UUID id) {
        return this.channels.removeIf(x-> x.getId().equals(id));
    }
}

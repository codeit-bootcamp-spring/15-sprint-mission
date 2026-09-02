package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService{
    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public Channel create(String name, String description) {
        Channel channel = new Channel(name, description);
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID id, String name, String description) {
        Channel channel = data.get(id);
        if (channel != null) {
            channel.update(name, description);
        }
        return channel;
    }

    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }

}

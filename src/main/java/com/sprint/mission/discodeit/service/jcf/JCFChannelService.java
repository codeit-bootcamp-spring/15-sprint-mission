package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public Channel create(String channelName) {
        Channel channel = new Channel(channelName);
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
    public Channel update(UUID channelId, String channelName) {
        Channel channel = read(channelId);
        channel.update(channelName);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        data.remove(channelId);
    }
}


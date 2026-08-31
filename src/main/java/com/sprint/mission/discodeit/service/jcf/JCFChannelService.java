package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;
    public JCFChannelService() {
        this.data = new HashMap<>();
    }
    public Channel create(String channelName) {
        Channel channel = new Channel(channelName);
        data.put(channel.getChannelId(), channel);
        return channel;
    }
    @Override
    public Channel read(UUID id) {
        return data.get(id);
    }
    @Override
    public List<Channel> readAll(){
        return data.values()
                .stream()
                .collect(Collectors.toCollection(ArrayList::new));
    }
    @Override
    public Channel update(UUID id, String channelName) {
        Channel channel = data.get(id);
        if (channel != null) {
            channel.update(channelName);
        }
        return channel;
    }
    @Override
    public Channel delete(UUID id) {
        return data.remove(id);
    }
}

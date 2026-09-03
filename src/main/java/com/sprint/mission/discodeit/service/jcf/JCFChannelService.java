package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    Map<UUID, Channel> channelMap = new HashMap<>();

    @Override
    public Channel getChannel(UUID id) {
        return channelMap.get(id);
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(channelMap.values());
    }

    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        channelMap.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel updateChannel(UUID id, String channelName) {
        Channel channel = channelMap.get(id);
        channel.updateChannelName(channelName);
        return channel;
    }

    @Override
    public Channel deleteChannel(UUID id) {
        return channelMap.remove(id);
    }


}


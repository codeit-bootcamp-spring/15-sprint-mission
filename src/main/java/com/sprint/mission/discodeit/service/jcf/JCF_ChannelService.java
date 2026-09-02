package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCF_ChannelService implements ChannelService {

    private final Map<UUID, Channel> data;

    public JCF_ChannelService() {
        this.data = new HashMap<>();

    //테스트용 채널입니다.(사용후 주석처리하세요)
    Channel testChannel = new Channel("testChannel");
    data.put(testChannel.getId(), testChannel);
    }

    @Override
    public Channel create(String channelName) {
        Channel channel = new Channel(channelName);
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel read(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> readAll() {
        return List.copyOf(data.values());
    }

    @Override
    public Channel update(UUID id, String channelName) {
        Channel channel = data.get(id);
        if (channel == null) {
            return null;
        }
        channel.setChannelName(channelName);
        return channel;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}

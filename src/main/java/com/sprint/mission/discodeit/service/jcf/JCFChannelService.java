package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final List<Channel> data;

    public JCFChannelService() {
        this.data = new ArrayList<>();
    }

    @Override
    public Channel create(Channel channel) {
        data.add(channel);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        return null;
    }

    @Override
    public List<Channel> findAll() {
        return List.of();
    }

    @Override
    public Channel update(UUID id, String name, ChannelType type) {
        return null;
    }

    @Override
    public Channel findByid(UUID id) {
        for (Channel channel : data) {
            if (channel.getId().equals(id)) {
                return channel;
            }
        }
        return null;
    }
    @Override
    public void  delete(UUID id) {
        Channel channel = findByid(id);

        if (channel != null) {
            data.remove(channel);
        }
    }


}

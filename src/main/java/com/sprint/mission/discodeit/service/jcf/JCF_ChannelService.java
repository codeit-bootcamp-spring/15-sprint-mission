package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class JCF_ChannelService implements ChannelService {

    @Override
    public Channel create(String channelName) {
        return null;
    }

    @Override
    public Channel read(UUID id) {
        return null;
    }

    @Override
    public List<Channel> readAll() {
        return List.of();
    }

    @Override
    public Channel update(UUID id, String channelName) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }
}

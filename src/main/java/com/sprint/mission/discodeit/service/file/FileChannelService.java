package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;

public class FileChannelService implements ChannelService {
    @Override
    public void create(Channel channel) {

    }

    @Override
    public Channel read(Channel channel) {
        return null;
    }

    @Override
    public List<Channel> readAll() {
        return List.of();
    }

    @Override
    public void update(Channel channel, String data) {

    }

    @Override
    public void delete(Channel channel) {

    }
}

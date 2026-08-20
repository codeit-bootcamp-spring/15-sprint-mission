package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelService {
    void create(Channel channel);
    Channel read(Channel channel);
    List<Channel> readAll();
    void update(Channel channel, String data);
    void delete(Channel channel);
}

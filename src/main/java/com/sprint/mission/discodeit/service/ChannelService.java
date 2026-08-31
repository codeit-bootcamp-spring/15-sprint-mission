package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;

public interface ChannelService {
    Channel create(ChannelType channelType, String channelName, String channelDescription);
    Channel read(String channelName);
    List<Channel> readAll();
    void update(Channel channel1, ChannelType channelType, String channelName, String channelDescription);
    void delete(Channel channel);
}

package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel create(String name, ChannelType channelType);
    Channel read(UUID id);
    List<Channel> readAll();
    Channel update(UUID id, String name, ChannelType channelType);
    void delete(UUID id);


}

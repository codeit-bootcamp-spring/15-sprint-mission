package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(String channelName);
    Channel read(UUID channelId);
    List<Channel> readAll();
    Channel update(UUID channelId, String channelName);
    void delete(UUID channelId);
}

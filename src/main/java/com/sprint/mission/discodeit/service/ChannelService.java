package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;


public interface ChannelService {
    Channel create(String channelName);
    Channel read(UUID id);
    List<Channel> readAll();
    Channel update(UUID id, String channelName);
    Channel delete(UUID id);
}

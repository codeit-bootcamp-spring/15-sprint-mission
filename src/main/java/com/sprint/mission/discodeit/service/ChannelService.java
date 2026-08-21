package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;


public interface ChannelService {
    Channel create(String Channel);
    Channel read(UUID id);
    List<Channel> readAll();
    Channel update(UUID id, String channelname);
    Channel delete(UUID id);
}

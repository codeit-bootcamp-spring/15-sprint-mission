package com.sprint.mission.discodeit.entity.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(String name, String topic);
    Channel getById(UUID id);
    List<Channel> readAll();
    Channel update(UUID id, String name, String topic);
    boolean deletebyID(UUID id);
}

package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.awt.*;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel create(Channel channel);

    Channel findById(UUID id);

    List<Channel> findAll();

    Channel update(UUID id, String name, ChannelType type);

    Channel findByid(UUID id);

    void delete(UUID id);
}


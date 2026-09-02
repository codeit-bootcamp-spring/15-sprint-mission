package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(ChannelCreateRequest cr);
    Channel find(UUID id);
    Channel findByChannelName(String channelName);
    List<Channel> readAll();
    void update(UUID id, ChannelUpdateRequest cu);
    void delete(UUID id);
}


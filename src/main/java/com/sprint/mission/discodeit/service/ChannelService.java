package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPublicChannel(String name, String description);
    Channel createPrivateChannel(List<User> userIds);
    ChannelResponse find(UUID id) throws IllegalArgumentException;
    ChannelResponse findByChannelName(String channelName) throws IllegalArgumentException;
    List<ChannelResponse> findAllByUserId(UUID userId);
    void update(ChannelUpdateRequest cu) throws IllegalArgumentException, IllegalStateException;
    void delete(UUID id) throws IllegalArgumentException;
}


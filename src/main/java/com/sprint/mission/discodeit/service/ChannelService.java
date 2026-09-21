package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  Channel createPublicChannel(ChannelCreateRequest cr);

  Channel createPrivateChannel(List<UUID> userIds);

  ChannelResponse find(UUID id);

  ChannelResponse findByChannelName(String channelName);

  List<ChannelResponse> findAllByUserId(UUID userId);

  Channel update(ChannelUpdateRequest cu);

  void delete(UUID id);
}


package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {

  boolean create(Channel channel);

  Channel find(UUID id);

  Channel findByChannelName(String channelName);

  List<Channel> findAll();

  boolean update(Channel channel);

  void delete(UUID id);
}

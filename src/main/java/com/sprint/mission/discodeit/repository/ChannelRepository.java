package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {

    Channel createChannel(Channel channel);

    Optional<Channel> getChannel(UUID id);

    List<Channel> getChannelAll();

    void deleteChannel(UUID id);

    boolean existsById(UUID id);
}

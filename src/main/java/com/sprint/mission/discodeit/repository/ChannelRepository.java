package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public interface ChannelRepository {
    void save(Channel channel);
    Channel load(UUID id);
    boolean delete(UUID id);
}

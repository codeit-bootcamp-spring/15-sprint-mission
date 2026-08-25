package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository {

    Channel save(Channel channel);
    Optional<Channel> findById(String id);
    List<Channel> findAll();
    void deleteById(String id);
    boolean existById(String id);
}

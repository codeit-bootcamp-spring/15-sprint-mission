package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {

    Channel create(String name, String description);
    Optional<Channel> read(UUID id);
    List<Channel> readAll();
    Channel update(UUID id, String name, String description);
    boolean delete(UUID id);

}

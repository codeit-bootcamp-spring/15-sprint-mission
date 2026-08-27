package com.sprint.mission.discodeit.service;

import java.util.UUID;

public interface ChannelService {

    void create(String name);
    void read();
    void update(UUID id, String name);
    void delete(UUID id);


}

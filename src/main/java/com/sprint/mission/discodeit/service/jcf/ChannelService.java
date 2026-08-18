package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.NitroLevel;

import java.util.UUID;

public interface ChannelService {

    void create(String name);
    void read();
    void update(UUID id, String name);
    void delete(UUID id);


}

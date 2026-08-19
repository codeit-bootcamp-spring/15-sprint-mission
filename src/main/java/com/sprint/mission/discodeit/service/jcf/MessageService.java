package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface MessageService {
    void create(UUID userid, String message);
    void read();
    void update(UUID id , String message);
    void delete(UUID id);
}

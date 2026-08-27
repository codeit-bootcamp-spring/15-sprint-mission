package com.sprint.mission.discodeit.service;

import java.util.UUID;

public interface MessageService {
    void create(UUID channelId,UUID userId, String message);
    void read();
    void update(UUID id , String message);
    void delete(UUID id);
}

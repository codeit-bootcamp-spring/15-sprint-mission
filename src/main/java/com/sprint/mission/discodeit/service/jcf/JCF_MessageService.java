package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class JCF_MessageService implements MessageService {
    @Override
    public Message create() {
        return null;
    }

    @Override
    public Message read(UUID id) {
        return null;
    }

    @Override
    public List<Message> readAll() {
        return List.of();
    }

    @Override
    public Message update(UUID id) {
        return null;
    }

    @Override
    public Message create(UUID channelId, String content) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }
}

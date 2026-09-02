package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCF_MessageService implements MessageService {

    private final Map<UUID, Message> data;

    public JCF_MessageService() {
        this.data = new HashMap<>();
    }

    @Override
    public Message create(UUID channelId, UUID userId, String content) {
        Message message = new Message(channelId, userId, content);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message read(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> readAll() {
        return List.copyOf(data.values());
    }

    @Override
    public Message update(UUID id, String content) {
        Message message = data.get(id);
        if (message == null) {
            return null;
        }
        message.setContent(content);
        return message;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}

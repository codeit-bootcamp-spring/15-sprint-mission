package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> data;

    public JCFMessageRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message read(UUID messageId) {
        return data.get(messageId);
    }

    @Override
    public List<Message> readAll() {
        return data.values().stream().toList();
    }

    @Override
    public void delete(UUID messageId) {
        data.remove(messageId);
    }
}
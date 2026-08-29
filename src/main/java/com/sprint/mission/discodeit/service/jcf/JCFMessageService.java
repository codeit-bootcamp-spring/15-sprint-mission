package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> data;

    public JCFMessageService() {
        this.data = new HashMap<>();
    }

    @Override
    public Message create(String contents, UUID userId, UUID channelId) {
        Message message = new Message(contents, userId, channelId);
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
    public Message update(UUID messageId, String contents) {
        Message message = read(messageId);
        message.update(contents);
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        data.remove(messageId);
    }
}

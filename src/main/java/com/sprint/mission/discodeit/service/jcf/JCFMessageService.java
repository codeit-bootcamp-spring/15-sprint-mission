package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    Map<UUID, Message> messageMap = new HashMap<>();


    @Override
    public Message createMessage(String content) {
        Message message = new Message(content);
        messageMap.put(message.getId(), message);
        return message;
    }

    @Override
    public Message getMessage(UUID id) {
        return messageMap.get(id);
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(messageMap.values());
    }

    @Override
    public Message updateMessage(UUID id, String content) {
        Message message = messageMap.get(id);
        message.updateContent(content);
        return message;
    }

    @Override
    public Message deleteMessage(UUID id) {
        return messageMap.remove(id);
    }
}

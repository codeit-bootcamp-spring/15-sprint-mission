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
    public Message findById(UUID id) {

        return data.get(id);
    }

    @Override
    public List<Message> findAll() {

        return List.copyOf(data.values());
    }

    @Override
    public void deleteById(UUID id) {

        data.remove(id);
    }
}
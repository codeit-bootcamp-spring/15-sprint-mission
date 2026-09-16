package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf", matchIfMissing = true)
public class JCFMessageRepository implements MessageRepository {
    private final List<Message> messages;

    public JCFMessageRepository() {
        this.messages = new ArrayList<>();
    }


    @Override
    public boolean create(Message message) {
        return this.messages.add(message);
    }

    @Override
    public Message find(UUID id) {
        return this.messages.stream().filter(x-> x.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Message> findByChannelId(UUID id) {
        return this.messages.stream().filter(x-> x.getChannelId().equals(id)).toList();
    }

    @Override
    public List<Message> findByAuthorId(UUID id) {
        return this.messages.stream().filter(x-> x.getAuthorId().equals(id)).toList();
    }

    @Override
    public List<Message> findAll() {
        return this.messages.stream().toList();
    }

    @Override
    public boolean update(Message message) {
        boolean isExist = this.messages.removeIf(x-> x.getId().equals(message.getId()));
        if (!isExist) return false;

        this.messages.add(message);
        return true;
    }

    @Override
    public boolean delete(UUID id) {
        return this.messages.removeIf(x-> x.getId().equals(id));
    }
}

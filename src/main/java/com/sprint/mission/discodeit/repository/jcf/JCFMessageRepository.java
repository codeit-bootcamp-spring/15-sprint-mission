package com.sprint.mission.discodeit.repository.jcf;
import org.springframework.stereotype.Repository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.*;
@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data = new HashMap<>();
    @Override
    public Message save(Message entity) {
        data.put(entity.getId(), entity);
        return entity;
    }
    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }
    @Override
    public List<Message> findAll() {
        return List.copyOf(data.values());
    }
    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
    public List<Message> findAllByChannelId(UUID channelId) {
        List<Message> result = new ArrayList<>();
        for (Message message : findAll()) {
            if (message.getChannelId().equals(channelId)) {
                result.add(message);
            }
        }
        return result;
    }
}

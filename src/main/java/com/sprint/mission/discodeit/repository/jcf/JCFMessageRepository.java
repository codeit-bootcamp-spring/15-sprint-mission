package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {

    final Map<UUID, Message> data = new HashMap<>();

    public JCFMessageRepository(){
    }


    @Override
    public Message save(Message message) {
        this.data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        List<Message> messageList = new ArrayList<>();

        for (Message entry : findAll()){
            if(entry.getChannelId().equals(channelId)){
                messageList.add(entry);
            }
        }

        return messageList;
    }

    @Override
    public List<Message> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void deleteById(UUID id) {
        this.data.remove(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }
}

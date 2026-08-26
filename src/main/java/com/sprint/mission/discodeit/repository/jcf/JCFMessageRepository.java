package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;

public class JCFMessageRepository implements MessageRepository {
    private final List<Message> data;

    public JCFMessageRepository() {
        this.data = new ArrayList<>();
    }

    @Override
    public boolean create(Message message) {
        try {
            this.data.add(message);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

/*    @Override
    public Message read(User user) {
        for (Message m : this.data) {
            if (m.getUser().getId().equals(user.getId())) {
                return m;
            }
        }
        return null;
    }*/

    @Override
    public List<Message> readAll() {
        return this.data.stream().toList();
    }

    @Override
    public boolean update(Message message) {
        for (Message m : this.data) {
            if (m.getId().equals(message.getId())) {
                m.setMessage(message.getMessage());
                m.setUpdatedAt(message.getUpdatedAt());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Message message) {
        for (Message m: this.data) {
            if (m.getId().equals(message.getId())) {
                this.data.remove(m);
                return true;
            }
        }
        return false;
    }
}

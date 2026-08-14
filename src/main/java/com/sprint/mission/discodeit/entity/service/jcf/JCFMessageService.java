package com.sprint.mission.discodeit.entity.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    public JCFMessageService() {
        this.data = new LinkedHashMap<>();
    }

    @Override
    public Message createMessage(String content) {
        Message message = new Message(content);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message getById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(UUID id, String content) {
        data.get(id).update(content);
        data.put(id, data.get(id));
        return data.get(id);
    }

    @Override
    public boolean deleteByID(UUID id) {
        return data.remove(id, data.get(id));
    }

    // data 필드를 활용해 생성, 조회, 수정, 삭제하는 메소드를 구현하세요.
}

package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageService {
    void create(Message message);
    Message read (Message message);
    List<Message> readAll();
    void update(Message message, String data);
    void delete(Message message);
}

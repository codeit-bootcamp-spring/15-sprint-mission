package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.*;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String mes, Channel channel, User user);
    List<Message> userReadAll(User user);
    List<Message> channelReadAll(Channel channel);
    Message read(UUID channelId, UUID userId);
    List<Message> readAll();
    void update(Message message, String mes);
    void delete(Message message);
}

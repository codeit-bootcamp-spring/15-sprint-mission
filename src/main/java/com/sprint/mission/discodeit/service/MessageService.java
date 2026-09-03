package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import java.util.*;

public interface MessageService {
        Message createMessage(String content);
        Message getMessage(UUID id);
        List<Message> getAllMessages();
        Message updateMessage(UUID id, String content);
        Message deleteMessage(UUID id);
}

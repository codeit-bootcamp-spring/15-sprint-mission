package com.sprint.mission.discodeit.entity.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.entity.service.jcf.JCFUserService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MessageService {
    Message createMessage(String content, User user, Channel channel);
    Message getById(UUID id);
    List<Message> readAll();
    Message update(UUID id, String content);
    boolean deleteByID(UUID id);

}

package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId){
        if (channelService.find(channelId) == null){
            throw new IllegalArgumentException("존재하지 않는 채널입니다. ID: " + channelId);
        }

        if (userService.find(authorId) == null){
            throw new IllegalArgumentException("존재하지 않는 사용자입니다. ID: " + authorId);
        }
        Message message = new Message(content, channelId, authorId);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message find(UUID id){
        return data.get(id);
    }

    @Override
    public List<Message> findAll(){
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(UUID id, String content){
        Message message = find(id);
        if (message != null) {
            message.update(content);
        }
        return message;
    }

    @Override
    public void delete(UUID id){
        data.remove(id);
    }
}

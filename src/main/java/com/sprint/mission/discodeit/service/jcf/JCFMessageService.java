package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> data;
    private final JCFChannelService jcfChannelService;
    private final JCFUserService jcfUserService;

    public JCFMessageService(JCFChannelService jcfChannelService, JCFUserService jcfUserService) {
        this.data = new HashMap<>();
        this.jcfChannelService = jcfChannelService;
        this.jcfUserService = jcfUserService;
    }

    public Message createMessage(UUID channelId, UUID authorId, String content){
        Channel channel = jcfChannelService.getChannelInfo(channelId);
        User user = jcfUserService.getUser(authorId);
        Message message = new Message(channelId,authorId,content);
        data.put(message.getId(),message);
        return message;
    }

    @Override
    public List<Message> getMessagesByChannel(UUID channelId) {
        return data.values().stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessagesByUser(UUID userId) {
        return data.values().stream()
                .filter(m -> m.getAuthorId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMessageContents(UUID uuid,String newContents) {
        Message message = findMessage(uuid);
        data.get(message.getId()).update(newContents);

    }

    @Override
    public void deleteMessage(UUID id) {
        Message message = findMessage(id);
        data.remove(message.getId());
    }


    private Message findMessage(UUID id) {
        Message message = data.get(id);
        if (message == null) {
            throw new IllegalArgumentException("존재하지 않는 메세지 입니다. id = " + id);
        }
        return message;
    }


}

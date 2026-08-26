package com.sprint.mission.discodeit.service.jcf;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private final List<Message> data;

    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.data = new ArrayList<>();
        this.userService = userService;
        this.channelService = channelService;

    }


    @Override
    public Message create(Message message) {
        data.add(message);
        return message;
    }

    @Override
    public Message findById(UUID id) {
        for (Message message : data) {
            if (message.getId().equals(id)) {
                return message;
            }
        }
        return null;
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Message update(UUID id, String content) {
        Message message = findById(id);

        if (message == null) {
            return null;
        }
        message.update(content);
        return message;
    }

    @Override
    public void delete(UUID id) {
        Message message = findById(id);

        if (message != null) {
            data.remove(message);
        }


    }

    @Override
    public void like(UUID messageId, UUID userId) {
        Message message = findById(messageId);

        if (message == null) {
            throw new IllegalArgumentException("메시지가 존재하지 않습니다.");

        }
        message.getLikeUserIds().add(userId);

    }
    @Override
    public Set<UUID> getlikeUserIds(UUID messageId) {
        Message message = findById(messageId);

        if (message == null) {
            throw new IllegalArgumentException("메시지가 존재하지 않습니다.");
        }

        return message.getLikeUserIds();
    }
    @Override
    public void unlike(UUID messageId, UUID userId) {
        Message message = findById(messageId);

        if (message == null) {
            throw new IllegalArgumentException("메시지가 존재하지 않습니다.");
        }
        message.getLikeUserIds().remove(userId);
    }

}

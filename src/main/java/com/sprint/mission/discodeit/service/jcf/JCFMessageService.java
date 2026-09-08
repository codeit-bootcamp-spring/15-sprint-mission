
package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

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
    public Message create(String content, UUID channelId, UUID authorId) {
        userService.findById(authorId).orElseThrow(() -> new IllegalArgumentException(
                "존재하지 않는 사용자입니다."));

        channelService.findById(channelId).orElseThrow(() -> new IllegalArgumentException(
                "존재하지 않는 채널입니다"));

        Message message = new Message(authorId, channelId, content);
        data.add(message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return data.stream().filter(message -> message.getId().equals(id)).findFirst();

    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Optional<Message> update(UUID id, String content) {
        Optional<Message> optionalMessage =findById(id);

        if (optionalMessage.isEmpty()) {
            return Optional.empty();
        }

      Message message = optionalMessage.get();
        message.setContent(content);
        return Optional.of(message);
    }

    @Override
    public void delete(UUID id) {
        data.removeIf(message -> message.getId().equals(id));

    }

    @Override
    public void like(UUID messageId, UUID userId) {

    }

    @Override
    public void unlike(UUID messageId, UUID userId) {

    }

    @Override
    public Set<UUID> getLikeUserIds(UUID messageId) {
        return Set.of();
    }

}


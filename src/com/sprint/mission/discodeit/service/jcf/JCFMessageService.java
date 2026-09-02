package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> data = new HashMap<>();
    private final UserService userService;
    private final ChannelService channelService;

    // 의존성 주입 (DI)
    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message create(String content, UUID authorId, UUID channelId) {
        // 작성자 및 채널 유효성 검증
        if (userService.read(authorId).isEmpty()) {
            throw new NoSuchElementException("작성자(User)를 찾을 수 없습니다: " + authorId);
        }
        if (channelService.read(channelId).isEmpty()) {
            throw new NoSuchElementException("채널(Channel)을 찾을 수 없습니다: " + channelId);
        }

        Message message = new Message(content, authorId, channelId);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<Message> readAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public Message update(UUID id, String content) {
        Message message = data.get(id);
        if (message != null) {
            message.update(content);
        }
        return message;
    }

    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }
}

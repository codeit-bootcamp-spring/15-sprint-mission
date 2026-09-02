package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public BasicMessageService(MessageRepository messageRepository,
                                ChannelRepository channelRepository,
                                UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Message createMessage(UUID channelId, UUID authorId, String content) {
        if (!channelRepository.existsById(channelId)) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다. id = " + channelId);
        }
        if (!userRepository.existsById(authorId)) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다. id = " + authorId);
        }
        Message message = new Message(channelId, authorId, content);
        return messageRepository.createMessage(message);
    }

    @Override
    public List<Message> getMessagesByChannel(UUID channelId) {
        return messageRepository.getMessageAll().stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessagesByUser(UUID userId) {
        return messageRepository.getMessageAll().stream()
                .filter(m -> m.getAuthorId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMessageContents(UUID uuid, String newContents) {
        Message message = findMessage(uuid);
        message.update(newContents);
        messageRepository.createMessage(message);
    }

    @Override
    public void deleteMessage(UUID id) {
        if (!messageRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 메세지 입니다. id = " + id);
        }
        messageRepository.deleteMessage(id);
    }

    private Message findMessage(UUID id) {
        return messageRepository.getMessage(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메세지 입니다. id = " + id));
    }
}

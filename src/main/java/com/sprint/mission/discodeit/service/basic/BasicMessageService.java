package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(String contents, UUID userId, UUID channelId) {
        Message message = new Message(contents, userId, channelId);
        return messageRepository.save(message);
    }

    @Override
    public Message read(UUID messageId) {
        return messageRepository.read(messageId);
    }

    @Override
    public List<Message> readAll() {
        return messageRepository.readAll();
    }

    @Override
    public Message update(UUID messageId, String contents) {
        Message message = messageRepository.read(messageId);
        message.update(contents);
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        messageRepository.delete(messageId);
    }
}
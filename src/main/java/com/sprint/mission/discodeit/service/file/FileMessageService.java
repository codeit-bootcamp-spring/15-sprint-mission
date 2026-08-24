package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {

    private final MessageRepository messageRepository;

    public FileMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(UUID channelId, UUID userId, String content) {

        Message message = new Message(channelId, userId, content);

        messageRepository.save(message);

        return message;
    }

    @Override
    public Message read(UUID id) {

        return messageRepository.findById(id);
    }

    @Override
    public List<Message> readAll() {

        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID id, String content) {

        Message message = messageRepository.findById(id);

        if (message == null) {
            return null;
        }

        message.setContent(content);

        messageRepository.save(message);

        return message;
    }

    @Override
    public void delete(UUID id) {

        messageRepository.deleteById(id);
    }
}
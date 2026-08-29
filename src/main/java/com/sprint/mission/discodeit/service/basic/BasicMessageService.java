package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;

import java.util.*;

public class BasicMessageService implements MessageService{

//    private final MessageRepository messageRepository = new FileMessageRepository();

    private final MessageRepository messageRepository;
    public BasicMessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    @Override
    public Message getMessage(UUID id) { return messageRepository.findById(id); }

    @Override
    public List<Message> getAllMessages() { return messageRepository.findAll(); }

    @Override
    public Message createMessage(String content) {
        Message message = new Message(content);
        return messageRepository.save(message);
    }

    @Override
    public Message updateMessage(UUID id, String content) {
        return messageRepository.update(id, content);
    }

    @Override
    public Message deleteMessage(UUID id) {
        return messageRepository.delete(id);
    }
}

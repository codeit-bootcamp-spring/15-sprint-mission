package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    private MessageRepository messageRepository;

    private final static BasicMessageService instance = new BasicMessageService();
    private BasicMessageService() { }
    public static BasicMessageService getInstance() {
        return instance;
    }

    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(UUID channelId, UUID userId, String messageString) {
        Message message = new Message(channelId,userId,messageString);
        return messageRepository.save(message);
    }

    @Override
    public Message read(UUID id) {
        try{
            return messageRepository.findById(id).get();
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("id 없음");
        }
    }

    @Override
    public List<Message> readAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID id, String messageString) {
        Message message;
        try{
            message = messageRepository.findById(id).get();
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("id 없음");
        }
        finally {

        }
        message.update(messageString);
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID id) {
        try{
            messageRepository.findById(id).get();
        }catch (NoSuchElementException e){
            throw new NoSuchElementException("id 없음");
        }
        finally {}
        messageRepository.deleteById(id);

    }
}

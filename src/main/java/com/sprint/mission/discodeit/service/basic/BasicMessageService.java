package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;



    @Override
    public Message create(UUID channelId, UUID userId, String messageString) {
        if(!channelRepository.existsById(channelId)){
            throw new NoSuchElementException("채널 id 없음 : "+ channelId);
        }

        if(!userRepository.existsById(userId)){
            throw new NoSuchElementException("유저 id 없음 : "+ userId);
        }


        Message message = new Message(channelId,userId,messageString);
        return messageRepository.save(message);
    }

    @Override
    public Message read(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("채널 id 없음 : " + id));
    }

    @Override
    public List<Message> readAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID id, String messageString) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("메세지 id 없음 : " + id));
        message.update(messageString);

        message.update(messageString);
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID id) {
        if (!messageRepository.existsById(id)) {
            throw new NoSuchElementException("메세지 id 없음 : " + id);
        }
        messageRepository.deleteById(id);

    }
}

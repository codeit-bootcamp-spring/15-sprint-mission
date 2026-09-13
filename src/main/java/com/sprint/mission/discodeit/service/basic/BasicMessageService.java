package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.Request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;


    @Override
    public Message create(MessageCreateRequest messageCreateRequest) {
        if(!channelRepository.existsById(messageCreateRequest.channelId())){
            throw new NoSuchElementException("채널 id 없음 : "+ messageCreateRequest.channelId());
        }

        if(!userRepository.existsById(messageCreateRequest.userId())){
            throw new NoSuchElementException("유저 id 없음 : "+ messageCreateRequest.userId());
        }


        Message message = new Message(messageCreateRequest.channelId(),messageCreateRequest.userId(),
                messageCreateRequest.messageString(), messageCreateRequest.binaryIds().orElse(null));
        return messageRepository.save(message);
    }

    @Override
    public Message find(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("채널 id 없음 : " + id));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId){
        return messageRepository.findAllByChannelId(channelId);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(MessageUpdateRequest messageUpdateRequest) {
        Message message = messageRepository.findById(messageUpdateRequest.id())
                .orElseThrow(() -> new NoSuchElementException("메세지 id 없음 : " + messageUpdateRequest.id()));
        message.update(messageUpdateRequest.messageString());
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("메시지 id 없음 : " + id));


        if(message.getBinaryIds()!=null){
            for(UUID entry : message.getBinaryIds()){
                if (binaryContentRepository.existsById(entry)) {
                    binaryContentRepository.deleteById(entry);
                }
            }
        }
        messageRepository.deleteById(id);

    }
}

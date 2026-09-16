package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageResponse create(MessageCreateRequest request) {
        if (channelRepository.read(request.channelId()) == null) {
            throw new NoSuchElementException("존재하지 않는 채널");
        }
        if (userRepository.read(request.authorId()) == null) {
            throw new NoSuchElementException("존재하지 않는 유저");
        }

        Message message = new Message(
                request.contents(),
                request.authorId(),
                request.channelId(),
                request.attachmentIds()
        );
        messageRepository.save(message);
        return toResponse(message);
    }

    @Override
    public MessageResponse read(UUID messageId) {
        Message message = messageRepository.read(messageId);
        if (message == null) {
            throw new NoSuchElementException("존재하지 않는 메시지");
        }
        return toResponse(message);
    }

    @Override
    public List<MessageResponse> readAllByChannelId(UUID channelId) {
        return messageRepository.readAllByChannelId(channelId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MessageResponse update(UUID messageId, MessageUpdateRequest request) {
        Message message = messageRepository.read(messageId);
        if (message == null) {
            throw new NoSuchElementException("존재하지 않는 메시지");
        }

        message.update(request.contents());
        messageRepository.save(message);
        return toResponse(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.read(messageId);
        if (message == null) {
            throw new NoSuchElementException("존재하지 않는 메시지");
        }

        if (message.getAttachmentIds() != null) {
            message.getAttachmentIds().forEach(binaryContentRepository::delete);
        }

        messageRepository.delete(messageId);
    }

    private MessageResponse toResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getContents(),
                message.getChannelId(),
                message.getUserId(),
                message.getAttachmentIds(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
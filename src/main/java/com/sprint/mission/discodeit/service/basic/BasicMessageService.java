package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.entity.Message;
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
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageDto create(MessageCreateRequest request) {

        userRepository.findById(request.authorId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 작성자입니다. ID: " + request.authorId()
                        )
                );

        channelRepository.findById(request.channelId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 채널입니다. ID: " + request.channelId()
                        )
                );

        List<UUID> attachmentIds;

        if (request.attachments() == null) {
            attachmentIds = List.of();
        } else {
            attachmentIds = request.attachments().stream()
                    .map(attachmentRequest -> {
                        BinaryContent binaryContent = new BinaryContent(
                                attachmentRequest.fileName(),
                                attachmentRequest.contentType(),
                                attachmentRequest.bytes()
                        );

                        return binaryContentRepository
                                .save(binaryContent)
                                .getId();
                    })
                    .toList();
        }

        Message message = new Message(
                request.content(),
                request.channelId(),
                request.authorId(),
                attachmentIds
        );

        Message savedMessage = messageRepository.save(message);

        return new MessageDto(
                savedMessage.getId(),
                savedMessage.getContent(),
                savedMessage.getChannelId(),
                savedMessage.getAuthorId(),
                savedMessage.getAttachmentIds(),
                savedMessage.getCreatedAt(),
                savedMessage.getUpdatedAt()
        );
    }

    @Override
    public MessageDto find(UUID id) {

        Message message = messageRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 메시지입니다. ID: " + id
                        )
                );

        return new MessageDto(
                message.getId(),
                message.getContent(),
                message.getChannelId(),
                message.getAuthorId(),
                message.getAttachmentIds(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }

    @Override
    public List<MessageDto> findAllByChannelId(UUID channelId) {

        channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 채널입니다. ID: " + channelId
                        )
                );

        return messageRepository.findAllByChannelId(channelId)
                .stream()
                .map(message ->
                        new MessageDto(
                                message.getId(),
                                message.getContent(),
                                message.getChannelId(),
                                message.getAuthorId(),
                                message.getAttachmentIds(),
                                message.getCreatedAt(),
                                message.getUpdatedAt()
                        )
                )
                .toList();
    }

    @Override
    public MessageDto update(MessageUpdateRequest request) {

        Message message = messageRepository.findById(request.id())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 메시지입니다. ID: " + request.id()
                        )
                );

        message.update(request.content());

        Message savedMessage = messageRepository.save(message);

        return new MessageDto(
                savedMessage.getId(),
                savedMessage.getContent(),
                savedMessage.getChannelId(),
                savedMessage.getAuthorId(),
                savedMessage.getAttachmentIds(),
                savedMessage.getCreatedAt(),
                savedMessage.getUpdatedAt()
        );
    }

    @Override
    public void delete(UUID id) {

        Message message = messageRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 메시지입니다. ID: " + id
                        )
                );

        message.getAttachmentIds()
                .forEach(binaryContentRepository::deleteById);

        messageRepository.deleteById(id);
    }

}
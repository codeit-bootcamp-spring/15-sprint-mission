package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.transaction.annotation.Transactional;
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

  private final BinaryContentService binaryContentService;

    @Override
    @Transactional
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
                .map(attachmentRequest ->
                    binaryContentService.create(attachmentRequest).id()
                )
                    .toList();
        }

        Message message = new Message(
                request.content(),
            channelRepository.findById(request.channelId())
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다.")),
            userRepository.findById(request.authorId())
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다.")),
            binaryContentRepository.findAllById(attachmentIds)
        );

        Message savedMessage = messageRepository.save(message);

        return new MessageDto(
                savedMessage.getId(),
                savedMessage.getContent(),
                savedMessage.getChannel().getId(),
                savedMessage.getAuthor().getId(),
                savedMessage.getAttachments().stream()
                    .map(BinaryContent::getId)
                    .toList(),
                savedMessage.getCreatedAt(),
                savedMessage.getUpdatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
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
                message.getChannel().getId(),
                message.getAuthor().getId(),
            message.getAttachments().stream()
                .map(BinaryContent::getId)
                .toList(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDto> findAllByChannelId(UUID channelId) {

        channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 채널입니다. ID: " + channelId
                        )
                );


        return messageRepository.findAllByChannel_Id(channelId)
                .stream()
                .map(message ->
                        new MessageDto(
                                message.getId(),
                                message.getContent(),
                                message.getChannel().getId(),
                                message.getAuthor().getId(),
                            message.getAttachments().stream()
                                .map(BinaryContent::getId)
                                .toList(),
                                message.getCreatedAt(),
                                message.getUpdatedAt()
                        )
                )
                .toList();
    }

    @Override
    @Transactional
    public MessageDto update(UUID id, MessageUpdateRequest request) {

        Message message = messageRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 메시지입니다. ID: " + id
                        )
                );

        message.update(request.content());

        Message savedMessage = messageRepository.save(message);

        return new MessageDto(
                savedMessage.getId(),
                savedMessage.getContent(),
                savedMessage.getChannel().getId(),
                savedMessage.getAuthor().getId(),
                savedMessage.getAttachments().stream()
                    .map(BinaryContent::getId)
                    .toList(),
                savedMessage.getCreatedAt(),
                savedMessage.getUpdatedAt()
        );
    }

  @Override
  @Transactional
  public void delete(UUID id) {

    Message message = messageRepository.findById(id)
        .orElseThrow(() ->
            new NoSuchElementException(
                "존재하지 않는 메시지입니다. ID: " + id
            )
        );

    List<UUID> attachmentIds = message.getAttachments().stream()
        .map(BinaryContent::getId)
        .toList();

    message.getAttachments().clear();

    messageRepository.flush();

    messageRepository.delete(message);

    messageRepository.flush();

    attachmentIds.forEach(binaryContentService::delete);
  }

}
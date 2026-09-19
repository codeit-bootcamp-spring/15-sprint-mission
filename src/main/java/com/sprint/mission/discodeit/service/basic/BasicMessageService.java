package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentRequest.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(
            MessageCreateRequest request,
            List<BinaryContentCreateRequest> attachments
    ) {

        if (!channelRepository.existsById(request.channelId())) {
            throw new ChannelNotFoundException("채널이 없습니다.");
        }

        if (!userRepository.existsById(request.authorId())) {
            throw new UserNotFoundException("작성자가 없습니다.");
        }

        List<UUID> attachmentIds = new ArrayList<>();

        if (attachments != null) {

            for (BinaryContentCreateRequest attachment : attachments) {

                BinaryContent binaryContent = new BinaryContent(
                        attachment.contentType(),
                        attachment.fileName(),
                        attachment.fileSize(),
                        attachment.content()
                );

                binaryContentRepository.save(binaryContent);

                attachmentIds.add(
                        binaryContent.getId()
                );
            }
        }

        Message message = new Message(
                request.channelId(),
                request.authorId(),
                request.content(),
                attachmentIds
        );

        messageRepository.save(message);

        return message;
    }

    @Override
    public Message read(UUID id) {

        return messageRepository.findById(id)
                .orElseThrow(() ->
                        new MessageNotFoundException("메시지가 없습니다."));
    }

    // 특정 채널의 메시지만 조회
    @Override
    public List<Message> findAllByChannelId(UUID channelId) {

        if (!channelRepository.existsById(channelId)) {
            throw new ChannelNotFoundException("채널이 없습니다.");
        }

        return messageRepository.findAllByChannelId(channelId);
    }

    // DTO를 이용한 메시지 수정
    @Override
    public Message update(UUID id, MessageUpdateRequest request) {

        Message message = messageRepository.findById(id)
                .orElseThrow(() ->
                        new MessageNotFoundException("메시지가 없습니다."));

        message.setContent(request.content());

        messageRepository.save(message);

        return message;
    }

    @Override
    public void delete(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() ->
                        new MessageNotFoundException("메시지가 없습니다."));

        List<UUID> attachmentIds =
                List.copyOf(message.getAttachmentIds());

        messageRepository.deleteById(id);

        for (UUID attachmentId : attachmentIds) {
            binaryContentRepository.deleteById(attachmentId);
        }
    }
}
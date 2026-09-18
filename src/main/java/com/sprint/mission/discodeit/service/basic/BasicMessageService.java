package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.MessageCreateDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public Message create(MessageCreateDto dto) {
        // 작성자 및 채널 존재 검증
        if (!userRepository.existsById(dto.authorId())) {
            throw new NoSuchElementException("작성자(User)를 찾을 수 없습니다: " + dto.authorId());
        }
        if (!channelRepository.existsById(dto.channelId())) {
            throw new NoSuchElementException("채널(Channel)을 찾을 수 없습니다: " + dto.channelId());
        }

        // 첨부파일 저장 및 ID 리스트 추출
        List<UUID> attachmentIds = new ArrayList<>();
        if (dto.attachments() != null && !dto.attachments().isEmpty()) {
            for (BinaryContentCreateDto fileDto : dto.attachments()) {
                BinaryContent content = new BinaryContent(
                        fileDto.fileName(),
                        (long) fileDto.bytes().length,
                        fileDto.contentType(),
                        fileDto.bytes()
                );
                attachmentIds.add(binaryContentRepository.save(content).getId());
            }
        }

        Message message = new Message(dto.content(), dto.authorId(), dto.channelId(), attachmentIds);
        return messageRepository.save(message);
    }

    @Override
    public Message find(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다: " + id));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }

    @Override
    public Message update(MessageUpdateDto dto) {
        Message message = messageRepository.findById(dto.id())
                .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다: " + dto.id()));

        message.update(dto.content());
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다: " + id));

        // 메시지에 연결된 첨부파일(BinaryContent) 모두 삭제
        if (message.getAttachmentIds() != null && !message.getAttachmentIds().isEmpty()) {
            for (UUID attachmentId : message.getAttachmentIds()) {
                binaryContentRepository.deleteById(attachmentId);
            }
        }

        messageRepository.deleteById(id);
    }
}
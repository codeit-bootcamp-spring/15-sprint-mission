package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.MessageCreateDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> data = new HashMap<>();
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    public JCFMessageService(UserRepository userRepository,
                             ChannelRepository channelRepository,
                             BinaryContentRepository binaryContentRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.binaryContentRepository = binaryContentRepository;
    }

    @Override
    public Message create(MessageCreateDto dto) {
        if (!userRepository.existsById(dto.authorId())) {
            throw new NoSuchElementException("작성자(User)를 찾을 수 없습니다: " + dto.authorId());
        }
        if (!channelRepository.existsById(dto.channelId())) {
            throw new NoSuchElementException("채널(Channel)을 찾을 수 없습니다: " + dto.channelId());
        }

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
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message find(UUID id) {
        Message message = data.get(id);
        if (message == null) {
            throw new NoSuchElementException("메시지를 찾을 수 없습니다: " + id);
        }
        return message;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public Message update(MessageUpdateDto dto) {
        Message message = data.get(dto.id());
        if (message == null) {
            throw new NoSuchElementException("메시지를 찾을 수 없습니다: " + dto.id());
        }
        message.update(dto.content());
        return message;
    }

    @Override
    public void delete(UUID id) {
        Message message = data.remove(id);
        if (message == null) {
            throw new NoSuchElementException("메시지를 찾을 수 없습니다: " + id);
        }

        // 첨부파일 삭제
        if (message.getAttachmentIds() != null && !message.getAttachmentIds().isEmpty()) {
            for (UUID attachmentId : message.getAttachmentIds()) {
                binaryContentRepository.deleteById(attachmentId);
            }
        }
    }
}
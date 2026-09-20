package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.exception.NotFoundException;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(MessageCreateRequest mcr, List<BinaryContentCreateRequest> bccr) {
        //일단 채널, 유저가 진짜 있는건지 확인하고.
        if (userRepository.find(mcr.authorId()) == null || channelRepository.find(mcr.channelId()) == null) {
            throw new IllegalArgumentException("저장할 수 없는 채널, 유저 정보가 주어졌습니다.");
        }

        List<UUID> attachments = new ArrayList<>();
        if (bccr != null) {
            for (BinaryContentCreateRequest b : bccr) {
                BinaryContent binaryContent = new BinaryContent(b.fileName(), b.contentType(), b.bytes());
                if (!binaryContentRepository.save(binaryContent)) {
                    throw new IllegalStateException("첨부파일이 정상적으로 저장되지 않았습니다.");
                }
                attachments.add(binaryContent.getId());
            }
        }

        Message message = new Message(mcr.content(), mcr.channelId(), mcr.authorId(), attachments);
        messageRepository.create(message);

        return message;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findByChannelId(channelId);
    }

    @Override
    public Message update(MessageUpdateRequest mur) {
        Message message = messageRepository.find(mur.id());
        if (message == null) {
            throw new NotFoundException("메시지를 찾을 수 없습니다.");
        }

        message.setMessage(mur.content());
        message.autoSetUpdatedAt();

        return messageRepository.update(message);
    }

    @Override
    public void delete(UUID messageId) {
        // 삭제가 가능한 객체인지 조회
        Message message = messageRepository.find(messageId);
        if (message == null) {
            throw new NotFoundException("유저를 찾을 수 없습니다.");
        }

        List<BinaryContent> byIds = binaryContentRepository.findByIds(message.getAttachmentIds());
        byIds.forEach(b -> binaryContentRepository.delete(b.getId()));

        messageRepository.delete(messageId);
    }
}

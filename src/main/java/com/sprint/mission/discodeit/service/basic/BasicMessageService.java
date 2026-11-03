package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
@Slf4j
@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final MessageMapper messageMapper;



    @Transactional
    @Override
    public MessageDto create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        Channel channel = channelRepository.findById(messageCreateRequest.channelId())
                .orElseThrow(() -> new NoSuchElementException("채널 id 없음 : "+ messageCreateRequest.channelId()));

        User user = userRepository.findById(messageCreateRequest.authorId())
                .orElseThrow(() -> new NoSuchElementException("유저 id 없음 : "+ messageCreateRequest.authorId()));

        List<BinaryContent> attachments = binaryContentCreateRequests.stream()
                .map(attachmentRequest -> {
                    String fileName = attachmentRequest.fileName();
                    String contentType = attachmentRequest.contentType();
                    byte[] bytes = attachmentRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length, contentType);
                    BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
                    binaryContentStorage.put(createdBinaryContent.getId(),bytes);
                    return createdBinaryContent;
                })
                .toList();

        Message message = new Message(channel,user,
                messageCreateRequest.content(), attachments);
        messageRepository.save(message);

        MessageDto dto = messageMapper.toDto(message);
        log.info("메세지 보내기 성공 : {}", dto.id());
        return dto;
    }


    @Transactional(readOnly = true)
    @Override
    public PageResponse<MessageDto> findAllByChannelId(
            UUID channelId,
            Instant cursor,
            Pageable pageable
    ) {

        Slice<Message> messages = messageRepository.findAllByChannelIdWithCursor(
                channelId, cursor, pageable);

        List<MessageDto> content = messages.getContent()
                .stream()
                .map(messageMapper::toDto)
                .toList();

        String nextCursor = null;

        if (messages.hasNext() && !messages.getContent().isEmpty()) {
            Message lastMessage = messages.getContent()
                    .get(messages.getNumberOfElements() - 1);

            nextCursor = lastMessage.getCreatedAt().toString();
        }

        return PageResponseMapper.from(
                content,
                nextCursor,
                messages.getSize(),
                messages.hasNext(),
                messageRepository.countByChannelId(channelId)
        );
    }

    @Transactional
    @Override
    public MessageDto update(UUID id,MessageUpdateRequest messageUpdateRequest) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("메세지 id 없음 : " + id));
        message.update(messageUpdateRequest.newContent());

        MessageDto dto = messageMapper.toDto(message);
        log.info("메세지 수정 성공 : {}", dto.id());
        return dto;
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("메시지 id 없음 : " + id));
        messageRepository.delete(message);
        log.info("메세지 삭제 성공 : {}", id);

    }
}

package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.base.*;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageMapper messageMapper;
  private final BinaryContentStorage binaryContentStorage;
  private final PageResponseMapper pageResponseMapper;

  public MessageDto create(MessageCreateRequest request,
      List<BinaryContentCreateRequest> attachments) {
    log.info("메시지 생성 요청 : authorId = {}, channelId = {}, attachmentCount = {}", request.authorId(),
        request.channelId(), attachments == null ? 0 : attachments.size());
    if (!channelRepository.existsById(request.channelId())) {
      throw new ChannelNotFoundException(request.channelId());
    }
    if (!userRepository.existsById(request.authorId())) {
      throw new UserNotFoundException(request.authorId());
    }
    List<BinaryContent> attachmentIds = new ArrayList<>();
    if (attachments != null) {
      for (BinaryContentCreateRequest attachment : attachments) {
        byte[] bytes = attachment.getBytes();
        BinaryContent file = binaryContentRepository.save(new BinaryContent(
            attachment.getFileName(), attachment.getContentType(), (long) bytes.length));
        binaryContentStorage.put(file.getId(), bytes);
        attachmentIds.add(file);
      }
    }
    Channel channel = channelRepository.findById(request.channelId()).orElseThrow(
        () -> new ChannelNotFoundException(request.channelId())
    );
    User user = userRepository.findById(request.authorId()).orElseThrow(
        () -> new UserNotFoundException(request.authorId())
    );
    Message message = new Message(request.content(), channel, user, attachmentIds);
    Message savedMessage = messageRepository.save(message);
    log.info(
        "메시지 생성 완료 : messageId = {}, authorId = {}, channelId = {}, attachmentIds = {}",
        savedMessage.getId(), user.getId(), channel.getId(),
        attachmentIds.stream().map(BinaryContent::getId).toList());
    return messageMapper.toDto(savedMessage);
  }

  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor,
      Pageable pageable) {
    // 커서 방식에는 "몇 번째 페이지" 개념이 없으니 page는 항상 0으로 고정
    Pageable firstPage = PageRequest.of(0, pageable.getPageSize(), pageable.getSort());

    Slice<Message> slice = (cursor == null)
        ? messageRepository.findAllByChannelId(channelId, firstPage)
        : messageRepository.findAllByChannelIdAndCreatedAtLessThan(channelId, cursor, firstPage);

    Slice<MessageDto> dtoSlice = slice.map(messageMapper::toDto);
    return pageResponseMapper.fromSlice(dtoSlice, MessageDto::createdAt);
  }

  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    log.info("메세지 수정 요청 : messageId = {}, updateContent = {}", messageId, request.content());
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new MessageNotFoundException(messageId));
    message.update(request.content());
    log.info("메세지 수정 완료 : messageId = {}, authorId = {}, newContent = {}", message.getId(),
        message.getAuthor(), message.getContent());
    return messageMapper.toDto(messageRepository.save(message));
  }

  public void delete(UUID messageId) {
    log.info("메세지 삭제 요청 : messageId = {}", messageId);
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new MessageNotFoundException(messageId));
    List<BinaryContent> files = new ArrayList<>(message.getAttachments());
    messageRepository.deleteById(messageId);
    binaryContentRepository.deleteAll(files);
    log.info("메세ㅣ 삭제 완료 : messageId = {}", messageId);
  }

}

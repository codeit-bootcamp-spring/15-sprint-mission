package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.base.*;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
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
    if (!channelRepository.existsById(request.channelId())) {
      throw new IllegalArgumentException("존재하지 않는 채널입니다. " + request.channelId());
    }
    if (!userRepository.existsById(request.authorId())) {
      throw new IllegalArgumentException("존재하지 않는 유저입니다. " + request.authorId());
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
    Channel channel = channelRepository.findById(request.channelId()).orElseThrow();
    User user = userRepository.findById(request.authorId()).orElseThrow();
    Message message = new Message(request.content(), channel, user, attachmentIds);
    return messageMapper.toDto(messageRepository.save(message));
  }

  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable) {
    Slice<MessageDto> slice = messageRepository.findAllByChannelId(channelId, pageable)
        .map(messageMapper::toDto);
    return pageResponseMapper.fromSlice(slice);
  }

  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Message"));
    message.update(request.content());
    return messageMapper.toDto(messageRepository.save(message));
  }

  public void delete(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Message"));
    List<BinaryContent> files = new ArrayList<>(message.getAttachments());
    messageRepository.deleteById(id);
    binaryContentRepository.deleteAll(files);

  }

}

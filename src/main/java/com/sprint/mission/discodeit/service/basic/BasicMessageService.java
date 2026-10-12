package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.MessageCreateDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.response.PageResponse; // 요구사항 패키지 경로
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final MessageMapper messageMapper;
  private final PageMapper pageMapper;

  @Override
  @Transactional
  public MessageDto create(MessageCreateDto dto) {
    // 1. 작성자 및 채널 엔티티 조회
    User author = userRepository.findById(dto.authorId())
        .orElseThrow(
            () -> new NoSuchElementException("작성자(User)를 찾을 수 없습니다: " + dto.authorId()));

    Channel channel = channelRepository.findById(dto.channelId())
        .orElseThrow(
            () -> new NoSuchElementException("채널(Channel)을 찾을 수 없습니다: " + dto.channelId()));

    // 2. 첨부파일 메타데이터 DB 저장 및 파일 스토리지 저장
    List<BinaryContent> attachments = new ArrayList<>();
    if (dto.attachments() != null && !dto.attachments().isEmpty()) {
      for (BinaryContentCreateDto fileDto : dto.attachments()) {
        BinaryContent content = new BinaryContent(
            fileDto.fileName(),
            (long) fileDto.bytes().length,
            fileDto.contentType()
        );
        BinaryContent savedContent = binaryContentRepository.save(content);

        // 스토리지에 실제 파일 바이너리 저장
        binaryContentStorage.put(savedContent.getId(), fileDto.bytes());
        attachments.add(savedContent);
      }
    }

    // 3. 메시지 엔티티 생성 및 저장
    Message message = new Message(dto.content(), channel, author, attachments);
    Message savedMessage = messageRepository.save(message);

    return messageMapper.toDto(savedMessage);
  }

  @Override
  public MessageDto find(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다: " + id));

    return messageMapper.toDto(message);
  }

  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable) {
    // 50개씩 최근 메시지 순(createdAt 내림차순) 정렬 조건
    Pageable sortedPageable = PageRequest.of(
        pageable.getPageNumber(),
        pageable.getPageSize() > 0 ? pageable.getPageSize() : 50,
        Sort.by(Sort.Direction.DESC, "createdAt")
    );

    // 총 개수가 필요 없으므로 Slice로 조회
    Slice<Message> slice = messageRepository.findAllByChannelIdOrderByCreatedAtDesc(channelId,
        sortedPageable);
    Slice<MessageDto> dtoSlice = slice.map(messageMapper::toDto);

    return pageMapper.toPageResponse(dtoSlice);
  }

  @Override
  @Transactional
  public MessageDto update(UUID id, MessageUpdateDto dto) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다: " + id));

    message.updateContent(dto.content());
    return messageMapper.toDto(message);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다: " + id));

    if (message.getAttachments() != null && !message.getAttachments().isEmpty()) {
      binaryContentRepository.deleteAll(message.getAttachments());
    }

    messageRepository.delete(message);
  }
}
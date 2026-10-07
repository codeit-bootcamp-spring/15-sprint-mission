package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.MessageCreateDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
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

  @Override
  @Transactional
  public Message create(MessageCreateDto dto) {
    // 1. 작성자 및 채널 엔티티 조회
    User author = userRepository.findById(dto.authorId())
        .orElseThrow(() -> new NoSuchElementException("작성자(User)를 찾을 수 없습니다: " + dto.authorId()));

    Channel channel = channelRepository.findById(dto.channelId())
        .orElseThrow(
            () -> new NoSuchElementException("채널(Channel)을 찾을 수 없습니다: " + dto.channelId()));

    // 2. 첨부파일 메타데이터 엔티티 저장 및 리스트 생성 (bytes 제외)
    List<BinaryContent> attachments = new ArrayList<>();
    if (dto.attachments() != null && !dto.attachments().isEmpty()) {
      for (BinaryContentCreateDto fileDto : dto.attachments()) {
        BinaryContent content = new BinaryContent(
            fileDto.fileName(),
            (long) fileDto.bytes().length,
            fileDto.contentType()
        );
        attachments.add(binaryContentRepository.save(content));
      }
    }

    // 3. 연관 엔티티들을 직접 참조하여 Message 생성 및 저장
    Message message = new Message(dto.content(), channel, author, attachments);
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
  @Transactional
  public Message update(UUID id, MessageUpdateDto dto) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다: " + id));

    // 변경 감지(Dirty Checking): updateContent 호출만으로 커밋 시 자동 UPDATE 반영
    message.updateContent(dto.content());
    return message;
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다: " + id));

    // 메시지와 연관된 첨부파일 메타데이터 삭제
    if (message.getAttachments() != null && !message.getAttachments().isEmpty()) {
      binaryContentRepository.deleteAll(message.getAttachments());
    }

    messageRepository.delete(message);
  }
}
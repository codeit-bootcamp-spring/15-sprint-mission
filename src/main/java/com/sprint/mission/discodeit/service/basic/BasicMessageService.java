package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.ApiError;
import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messages;
  private final BinaryContentRepository binaries;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  public Message create(MessageCreateRequest request,
      List<BinaryContentCreateRequest> attachments) {
    if (!channelRepository.existsById(request.channelId())) {
      throw new IllegalArgumentException("존재하지 않는 채널입니다. " + request.channelId());
    }
    if (!userRepository.existsById(request.authorId())) {
      throw new IllegalArgumentException("존재하지 않는 유저입니다. " + request.authorId());
    }
    /*if (!channelRepository.existsUserByChannelId(request.channelId(), request.authorId())) {
      throw new IllegalArgumentException("채널에 포함되지 않은 유저입니다. " + request.authorId());
    }*/
    List<UUID> attachmentIds = new ArrayList<>();
    if (attachments != null) {
      for (BinaryContentCreateRequest attachment : attachments) {
        BinaryContent file = new BinaryContent(attachment.getFileName(),
            attachment.getContentType(), attachment.getBytes());
        binaries.save(file);
        attachmentIds.add(file.getId());
      }
    }
    Message message = new Message(request.channelId(), request.authorId(), request.content(),
        attachmentIds);
    return messages.save(message);
  }

  public List<Message> findAllByChannelId(UUID channelId) {
    return messages.findAllByChannelId(channelId);
  }

  public Message update(UUID messageId, MessageUpdateRequest request) {
    Message message = messages.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Message"));
    message.update(request.content());
    return messages.save(message);
  }

  public void delete(UUID id) {
    Message message = messages.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Message"));
    for (UUID attachmentId : message.getAttachmentIds()) {
      binaries.deleteById(attachmentId);
    }
    messages.deleteById(id);
  }

}

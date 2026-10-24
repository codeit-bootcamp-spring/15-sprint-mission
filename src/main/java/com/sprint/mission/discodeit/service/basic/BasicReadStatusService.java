package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.base.*;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository reads;
  private final UserRepository users;
  private final ChannelRepository channels;
  private final ReadStatusMapper readStatusMapper;

  public ReadStatusDto create(ReadStatusCreateRequest request) {
    User user = users.findById(request.userId())
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 User"));
    Channel channel = channels.findById(request.channelId())
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Channel"));
    Optional<ReadStatus> existing = reads.findByUserIdAndChannelId(request.userId(),
        request.channelId());
    // 읽음 상태를 새로 만들더라도 PRIVATE의 참여자 목록은 바꾸지 않습니다.
    if (channel.getType() == Channel.ChannelType.PRIVATE && existing.isEmpty()) {
      throw new IllegalArgumentException("PRIVATE 채널의 참여자가 아닙니다.");
    }
    if (existing.isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 읽음 상태입니다.");
    }
    return readStatusMapper.toDto(reads.save(new ReadStatus(user, channel, request.lastReadAt())));
  }

  @Transactional(readOnly = true)
  public ReadStatusDto find(UUID id) {
    return readStatusMapper.toDto(reads.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 ReadStatus: " + id)));
  }

  @Transactional(readOnly = true)
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return reads.findAllByUserId(userId).stream().map(readStatusMapper::toDto).toList();
  }

  @Transactional(readOnly = true)
  @Override
  public List<ReadStatusDto> findAllByChannelId(UUID channelId) {
    return reads.findAllByChannelId(channelId).stream().map(readStatusMapper::toDto).toList();
  }

  private ReadStatus getEntity(UUID id) {
    return reads.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 ReadStatus: " + id));
  }

  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    ReadStatus status = getEntity(readStatusId);
    status.update(request.newLastReadAt());
    return readStatusMapper.toDto(reads.save(status));
  }

  public void delete(UUID id) {
    reads.deleteById(id);
  }

}

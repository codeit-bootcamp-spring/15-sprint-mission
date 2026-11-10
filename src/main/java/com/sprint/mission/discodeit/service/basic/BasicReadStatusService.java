package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.base.*;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.NotChannelParticipantException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository reads;
  private final UserRepository users;
  private final ChannelRepository channels;
  private final ReadStatusMapper readStatusMapper;

  public ReadStatusDto create(ReadStatusCreateRequest request) {
    log.info("읽음 상태 생성 요청 : userId = {}, channelId = {}, lastReadAt = {}", request.userId(),
        request.channelId(), request.lastReadAt());
    User user = users.findById(request.userId())
        .orElseThrow(() -> new UserNotFoundException(request.userId()));
    Channel channel = channels.findById(request.channelId())
        .orElseThrow(() -> new ChannelNotFoundException(request.channelId()));
    Optional<ReadStatus> existing = reads.findByUserIdAndChannelId(request.userId(),
        request.channelId());
    // 읽음 상태를 새로 만들더라도 PRIVATE의 참여자 목록은 바꾸지 않습니다.
    if (channel.getType() == Channel.ChannelType.PRIVATE && existing.isEmpty()) {
      throw new NotChannelParticipantException(request.userId(), request.channelId());
    }
    if (existing.isPresent()) {
      throw new ReadStatusAlreadyExistsException();
    }
    ReadStatus readStatus = reads.save(new ReadStatus(user, channel, request.lastReadAt()));
    ReadStatusDto result = readStatusMapper.toDto(readStatus);
    log.info("읽음 상태 생성 완료 : readStatusId = {}, userId = {}, channelId = {}", readStatus.getId(),
        user.getId(), channel.getId());
    return result;
  }

  @Transactional(readOnly = true)
  public ReadStatusDto find(UUID id) {
    return readStatusMapper.toDto(reads.findById(id)
        .orElseThrow(() -> new ReadStatusNotFoundException(id)));
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
        .orElseThrow(() -> new ReadStatusNotFoundException(id));
  }

  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    ReadStatus status = getEntity(readStatusId);
    status.update(request.newLastReadAt());
    return readStatusMapper.toDto(reads.save(status));
  }

  public void delete(UUID id) {
    log.info("읽음 상태 삭제 요청 : readStatusId = {}", id);
    reads.deleteById(id);
  }

}

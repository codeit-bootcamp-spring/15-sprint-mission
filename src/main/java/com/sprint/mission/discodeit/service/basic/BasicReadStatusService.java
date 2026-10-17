package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  @Transactional
  public ReadStatus create(ReadStatusCreateRequest request) {
    // 1. User 조회
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다: " + request.userId()));

    // 2. Channel 조회
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다: " + request.channelId()));

    // 3. 동일한 (User, Channel) 중복 등록 검증
    if (readStatusRepository.existsByUserIdAndChannelId(request.userId(), request.channelId())) {
      throw new IllegalArgumentException("해당 채널에 대한 사용자의 읽음 상태가 이미 존재합니다.");
    }

    // 4. lastReadAt이 null이면 현재 시각(Instant.now())으로 설정 (NOT NULL 방어)
    Instant lastReadAt = request.lastReadAt() != null ? request.lastReadAt() : Instant.now();

    ReadStatus readStatus = new ReadStatus(
        user,
        channel,
        lastReadAt
    );

    return readStatusRepository.save(readStatus);
  }

  @Override
  public ReadStatus find(UUID id) {
    return readStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("ReadStatus가 존재하지 않습니다: " + id));
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException("사용자가 존재하지 않습니다: " + userId);
    }
    return readStatusRepository.findAllByUserId(userId);
  }

  @Override
  @Transactional
  public ReadStatus update(UUID id, ReadStatusUpdateRequest request) {
    ReadStatus readStatus = readStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("수정할 ReadStatus가 존재하지 않습니다: " + id));

    // lastReadAt이 null이면 현재 시각(Instant.now())으로 설정 (NOT NULL 방어)
    Instant lastReadAt = request.lastReadAt() != null ? request.lastReadAt() : Instant.now();

    // 엔티티 필드 수정 (Dirty Checking)
    readStatus.updateLastReadAt(lastReadAt);

    return readStatus;
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    ReadStatus readStatus = readStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("삭제할 ReadStatus가 존재하지 않습니다: " + id));

    readStatusRepository.delete(readStatus);
  }
}
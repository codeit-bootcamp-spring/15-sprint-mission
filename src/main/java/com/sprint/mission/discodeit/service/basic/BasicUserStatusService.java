package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserStatus create(UserStatusCreateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다: " + request.userId()));

    UserStatus userStatus = new UserStatus(user, request.lastActiveAt());
    return userStatusRepository.save(userStatus);
  }

  @Override
  @Transactional
  public UserStatus update(UUID userId, Instant lastActiveAt) {
    return userStatusRepository.findByUserId(userId)
        .map(status -> {
          status.updateLastActiveAt(lastActiveAt);
          return status;
        })
        .orElseGet(() -> {
          User user = userRepository.findById(userId)
              .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다: " + userId));

          UserStatus newStatus = new UserStatus(user, lastActiveAt);
          return userStatusRepository.save(newStatus);
        });
  }

  @Override
  public UserStatus findByUserId(UUID userId) {
    return userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("사용자 상태를 찾을 수 없습니다: " + userId));
  }
}
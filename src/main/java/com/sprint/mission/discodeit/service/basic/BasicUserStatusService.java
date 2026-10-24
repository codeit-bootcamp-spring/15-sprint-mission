package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.base.*;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;
  private final UserMapper userMapper;

  public UserStatusDto create(UserStatusCreateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 User"));
    if (userStatusRepository.findByUserId(request.userId()).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 접속 상태입니다.");
    }
    return userStatusMapper.toDto(
        userStatusRepository.save(new UserStatus(user, request.lastActiveAt())));
  }

  @Transactional(readOnly = true)
  public UserStatusDto find(UUID id) {
    return userStatusMapper.toDto(userStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 UserStatus: " + id)));
  }

  @Transactional(readOnly = true)
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream().map(userStatusMapper::toDto).toList();
  }

  private UserStatus getEntity(UUID id) {
    return userStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 UserStatus: " + id));
  }

  public UserStatusDto update(UserStatusUpdateRequest request) {
    UserStatus status = getEntity(request.id());
    status.update();
    return userStatusMapper.toDto(userStatusRepository.save(status));
  }

  public UserStatusDto updateByUserId(UUID userId) {
    UserStatus status = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("접속 상태가 없습니다."));
    status.update();
    return userStatusMapper.toDto(userStatusRepository.save(status));
  }

  public void delete(UUID id) {
    userStatusRepository.deleteById(id);
  }
}

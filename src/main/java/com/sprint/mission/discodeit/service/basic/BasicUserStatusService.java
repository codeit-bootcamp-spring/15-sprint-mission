package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.base.*;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
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
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;
  private final UserMapper userMapper;

  public UserStatusDto create(UserStatusCreateRequest request) {
    log.info("유저 상태 생성 요청 : userId = {}", request.userId());
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new UserNotFoundException(request.userId()));
    if (userStatusRepository.findByUserId(request.userId()).isPresent()) {
      throw new UserStatusAlreadyExistsException(request.userId());
    }
    return userStatusMapper.toDto(
        userStatusRepository.save(new UserStatus(user, request.lastActiveAt())));
  }

  @Transactional(readOnly = true)
  public UserStatusDto find(UUID id) {
    return userStatusMapper.toDto(userStatusRepository.findById(id)
        .orElseThrow(() -> UserStatusNotFoundException.byId(id)));
  }

  @Transactional(readOnly = true)
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream().map(userStatusMapper::toDto).toList();
  }

  private UserStatus getEntity(UUID id) {
    return userStatusRepository.findById(id)
        .orElseThrow(() -> UserStatusNotFoundException.byId(id));
  }

  public UserStatusDto update(UserStatusUpdateRequest request) {
    UserStatus status = getEntity(request.id());
    status.update();
    return userStatusMapper.toDto(userStatusRepository.save(status));
  }

  public UserStatusDto updateByUserId(UUID userId) {
    log.info("유저 상태 업데이트 요청 : userId = {}", userId);
    UserStatus status = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> UserStatusNotFoundException.byUserId(userId));
    status.update();
    log.info("유저 상태 업데이트 완료 : userId = {}", userId);
    return userStatusMapper.toDto(userStatusRepository.save(status));
  }

  public void delete(UUID id) {
    userStatusRepository.deleteById(id);
  }
}

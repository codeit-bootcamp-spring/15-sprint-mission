package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.base.*;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicUserService implements UserService {

  private final UserRepository users;
  private final BinaryContentRepository binaries;
  private final UserStatusRepository statuses;
  private final UserMapper userMapper;
  private final BinaryContentStorage binaryContentStorage;

  public UserDto create(UserCreateRequest request, BinaryContentCreateRequest profile) {
    checkUnique(null, request.username(), request.email());
    BinaryContent image = null;
    if (profile != null) {
      byte[] bytes = profile.getBytes();
      image = binaries.save(new BinaryContent(
          profile.getFileName(), profile.getContentType(), (long) bytes.length));
      binaryContentStorage.put(image.getId(), bytes);
    }
    if (image != null) {
      binaries.save(image);
    }
    User user = new User(request.username(), request.email(), request.password(),
        image == null ? null : image);
    UserStatus status = new UserStatus(user, Instant.now());
    user.setStatus(status);
    users.save(user);
    return userMapper.toDto(user);
  }

  @Transactional(readOnly = true)
  public UserDto find(UUID id) {
    return userMapper.toDto(users.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 User")));
  }

  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    List<UserDto> result = new ArrayList<>();
    for (User user : users.findAll()) {
      result.add(userMapper.toDto(user));
    }
    return result;
  }

  public UserDto update(UUID userId, UserUpdateRequest request,
      BinaryContentCreateRequest profile) {
    // 1. DTO의 ID로 기존 사용자를 조회합니다.
    User user = users.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 User"));
    checkUnique(user.getId(), request.newUsername(), request.newEmail());
    BinaryContent image = null;
    if (profile != null) {
      byte[] bytes = profile.getBytes();
      image = binaries.save(new BinaryContent(
          profile.getFileName(), profile.getContentType(), (long) bytes.length));
      binaryContentStorage.put(image.getId(), bytes);
    }
    BinaryContent previous = user.getProfile();
    if (image != null) {
      binaries.save(image);
    }
    // 2. DTO의 새 값을 기존 User에 반영합니다.
    user.update(request.newUsername(), request.newEmail(), request.newPassword());
    if (image != null) {
      user.replaceProfile(image);
    }
    users.save(user);
    // 파일에서 읽은 객체는 수정 후 다시 저장해야 파일에도 반영됩니다.
    if (image != null && previous != null) {
      binaries.delete(previous);
    }
    return userMapper.toDto(user);
  }

  public void delete(UUID id) {
    // 1. 사용자에게 연결된 프로필 ID를 확보합니다.
    User user = users.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 User"));
    // 2. 프로필이 있는 경우 그 파일의 ID로 삭제합니다.
    if (user.getProfile() != null) {
      binaries.deleteById(user.getProfile().getId());
    }
    // 3. 접속 상태의 ID는 사용자 ID와 다릅니다.
    Optional<UserStatus> status = statuses.findByUserId(id);
    if (status.isPresent()) {
      statuses.deleteById(status.get().getId());
    }
    // 4. 관련 데이터 정리 후 사용자를 삭제합니다.
    users.deleteById(id);
  }

  private void checkUnique(UUID ownId, String username, String email) {
    for (User other : users.findAll()) {
      if (other.getId().equals(ownId)) {
        continue;
      }
      if (username != null && username.equals(other.getUsername())) {
        throw new IllegalArgumentException("중복 newUsername");
      }
      if (email != null && email.equals(other.getEmail())) {
        throw new IllegalArgumentException("중복 newEmail");
      }
    }
  }
}

package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository users;
  private final BinaryContentRepository binaries;
  private final UserStatusRepository statuses;

  public UserResponse create(UserCreateRequest request, BinaryContentCreateRequest profile) {
    checkUnique(null, request.username(), request.email());
    BinaryContent image = profile == null ? null
        : new BinaryContent(profile.getFileName(), profile.getContentType(), profile.getBytes());
    if (image != null) {
      binaries.save(image);
    }
    User user = new User(request.username(), request.email(), request.password(),
        image == null ? null : image.getId());
    users.save(user);
    statuses.save(new UserStatus(user.getId(), Instant.now()));
    return toResponse(user);
  }

  public UserResponse find(UUID id) {
    return toResponse(users.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 User")));
  }

  public List<UserResponse> findAll() {
    List<UserResponse> result = new ArrayList<>();
    for (User user : users.findAll()) {
      result.add(toResponse(user));
    }
    return result;
  }

  public UserResponse update(UserUpdateRequest request, BinaryContentCreateRequest profile) {
    // 1. DTO의 ID로 기존 사용자를 조회합니다.
    User user = users.findById(request.id())
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 User"));
    checkUnique(user.getId(), request.username(), request.email());
    BinaryContent image = profile == null ? null
        : new BinaryContent(profile.getFileName(), profile.getContentType(), profile.getBytes());
    UUID previous = user.getProfileId();
    if (image != null) {
      binaries.save(image);
    }
    // 2. DTO의 새 값을 기존 User에 반영합니다.
    user.update(request.username(), request.email(), request.password());
    if (image != null) {
      user.replaceProfile(image.getId());
    }
    users.save(user);
    // 파일에서 읽은 객체는 수정 후 다시 저장해야 파일에도 반영됩니다.
    if (image != null && previous != null) {
      binaries.deleteById(previous);
    }
    return toResponse(user);
  }

  public void delete(UUID id) {
    // 1. 사용자에게 연결된 프로필 ID를 확보합니다.
    User user = users.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 User"));
    // 2. 프로필이 있는 경우 그 파일의 ID로 삭제합니다.
    if (user.getProfileId() != null) {
      binaries.deleteById(user.getProfileId());
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
        throw new IllegalArgumentException("중복 username");
      }
      if (email != null && email.equals(other.getEmail())) {
        throw new IllegalArgumentException("중복 email");
      }
    }
  }

  private UserResponse toResponse(User user) {
    boolean online = false;
    Optional<UserStatus> status = statuses.findByUserId(user.getId());
    if (status.isPresent()) {
      online = status.get().isOnline();
    }
    return new UserResponse(user.getId(), user.getUsername(), user.getEmail(),
        user.getCreatedAt(), user.getUpdatedAt(), user.getProfileId(), online);
  }
}

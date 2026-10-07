package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.UserCreateDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
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
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  @Transactional
  public UserResponseDto create(UserCreateDto dto) {
    // 1. 유효성 검증
    if (userRepository.existsByUsername(dto.username())) {
      throw new IllegalArgumentException("이미 존재하는 username입니다: " + dto.username());
    }
    if (userRepository.existsByEmail(dto.email())) {
      throw new IllegalArgumentException("이미 존재하는 email입니다: " + dto.email());
    }

    // 2. 프로필 메타데이터 저장 (bytes 제외)
    BinaryContent profile = null;
    if (dto.profileImage() != null) {
      BinaryContentCreateDto imgDto = dto.profileImage();
      profile = binaryContentRepository.save(new BinaryContent(
          imgDto.fileName(),
          (long) imgDto.bytes().length,
          imgDto.contentType()
      ));
    }

    // 3. User 엔티티 생성
    User user = new User(dto.username(), dto.email(), dto.password(), profile);

    // 4. UserStatus 생성 후 연관관계 설정 (CascadeType.ALL로 user 저장 시 함께 저장됨)
    UserStatus status = new UserStatus(user, Instant.now());
    user.updateStatus(status);

    User savedUser = userRepository.save(user);
    return UserResponseDto.of(savedUser, status.isOnline());
  }

  @Override
  public UserResponseDto find(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("해당 유저를 찾을 수 없습니다: " + id));

    return UserResponseDto.of(user, isUserOnline(user));
  }

  @Override
  public List<UserResponseDto> findAll() {
    return userRepository.findAll().stream()
        .map(user -> UserResponseDto.of(user, isUserOnline(user)))
        .toList();
  }

  @Override
  @Transactional
  public UserResponseDto update(UUID id, UserUpdateDto dto) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("해당 유저를 찾을 수 없습니다: " + id));

    BinaryContent profile = user.getProfile();

    // 새 프로필 이미지가 들어온 경우 교체
    if (dto.newProfileImage() != null) {
      if (profile != null) {
        binaryContentRepository.delete(profile);
      }
      BinaryContentCreateDto newImgDto = dto.newProfileImage();
      profile = binaryContentRepository.save(new BinaryContent(
          newImgDto.fileName(),
          (long) newImgDto.bytes().length,
          newImgDto.contentType()
      ));
    }

    // 변경 감지(Dirty Checking): updateProfile만 호출하면 트랜잭션 커밋 시 자동 반영 (save 호출 불필요)
    user.updateProfile(dto.username(), dto.email(), profile);

    return UserResponseDto.of(user, isUserOnline(user));
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("해당 유저를 찾을 수 없습니다: " + id));

    // 1. 프로필 이미지 삭제 (ON DELETE SET NULL 정책 대응)
    if (user.getProfile() != null) {
      binaryContentRepository.delete(user.getProfile());
    }

    // 2. UserStatus는 cascade = CascadeType.ALL, orphanRemoval = true 설정에 의해 자동 삭제됨
    userRepository.delete(user);
  }

  private boolean isUserOnline(User user) {
    return user.getStatus() != null && user.getStatus().isOnline();
  }
}
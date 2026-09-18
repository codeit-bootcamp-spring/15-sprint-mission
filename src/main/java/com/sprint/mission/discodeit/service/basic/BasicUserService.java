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
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserResponseDto create(UserCreateDto dto) {
        // 1. username, email 중복 체크
        if (userRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("이미 존재하는 username입니다: " + dto.username());
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("이미 존재하는 email입니다: " + dto.email());
        }

        // 2. 선택적 프로필 이미지 저장
        UUID profileImageId = null;
        if (dto.profileImage() != null) {
            BinaryContentCreateDto imgDto = dto.profileImage(); // <-- 여기서 꺼냄

            BinaryContent profile = new BinaryContent(
                    imgDto.fileName(),
                    (long) imgDto.bytes().length,
                    imgDto.contentType(),
                    imgDto.bytes()
            );
            profileImageId = binaryContentRepository.save(profile).getId();
        }

        // 3. User 엔티티 생성 및 저장
        User user = new User(dto.username(), dto.email(), dto.password(), profileImageId);
        User savedUser = userRepository.save(user);

        // 4. UserStatus 생성 및 저장 (기본 접속 시간: 현재 시간)
        UserStatus userStatus = new UserStatus(savedUser.getId(), Instant.now());
        userStatusRepository.save(userStatus);

        return UserResponseDto.of(savedUser, userStatus.isOnline());
    }

    @Override
    public UserResponseDto find(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 유저를 찾을 수 없습니다: " + id));

        return UserResponseDto.of(user, checkOnline(user.getId()));
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> UserResponseDto.of(user, checkOnline(user.getId())))
                .toList();
    }

    @Override
    public UserResponseDto update(UserUpdateDto dto) {
        User user = userRepository.findById(dto.id())
                .orElseThrow(() -> new NoSuchElementException("해당 유저를 찾을 수 없습니다: " + dto.id()));

        UUID profileImageId = user.getProfileImageId();

        // 새 프로필 이미지가 들어온 경우 기존 이미지 교체/삭제 처리
        if (dto.newProfileImage() != null) {
            // 1. 기존 프로필 이미지가 존재했다면 삭제
            if (profileImageId != null) {
                binaryContentRepository.deleteById(profileImageId);
            }

            // 2. 새 프로필 이미지 꺼내기
            BinaryContentCreateDto newImgDto = dto.newProfileImage();

            // 3. BinaryContent 엔티티 생성 (생성자 순서: fileName, size, contentType, bytes)
            BinaryContent newProfile = new BinaryContent(
                    newImgDto.fileName(),
                    (long) newImgDto.bytes().length,
                    newImgDto.contentType(),
                    newImgDto.bytes()
            );
            // 4. 저장 후 새 ID 갱신
            profileImageId = binaryContentRepository.save(newProfile).getId();
        }

        user.update(dto.username(), dto.email(), dto.password(), profileImageId);
        User updatedUser = userRepository.save(user);

        return UserResponseDto.of(updatedUser, checkOnline(updatedUser.getId()));
    }

    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 유저를 찾을 수 없습니다: " + id));

        // 1. 프로필 이미지(BinaryContent) 연관 삭제
        if (user.getProfileImageId() != null) {
            binaryContentRepository.deleteById(user.getProfileImageId());
        }

        // 2. UserStatus 연관 삭제
        userStatusRepository.findByUserId(id)
                .ifPresent(status -> userStatusRepository.deleteById(status.getId()));

        // 3. User 본체 삭제
        userRepository.deleteById(id);
    }

    private boolean checkOnline(UUID userId) {
        return userStatusRepository.findByUserId(userId)
                .map(UserStatus::isOnline)
                .orElse(false);
    }
}
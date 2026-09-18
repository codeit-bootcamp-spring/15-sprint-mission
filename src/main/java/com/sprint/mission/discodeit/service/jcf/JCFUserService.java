package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.UserCreateDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;

import java.time.Instant;
import java.util.*;

public class JCFUserService implements UserService {

    private final Map<UUID, User> userData = new HashMap<>();
    private final Map<UUID, UserStatus> userStatusData = new HashMap<>();
    private final Map<UUID, BinaryContent> binaryContentData = new HashMap<>();

    @Override
    public UserResponseDto create(UserCreateDto dto) {
        // 1. username 및 email 중복 검사
        boolean usernameExists = userData.values().stream()
                .anyMatch(u -> u.getUsername().equals(dto.username()));
        if (usernameExists) {
            throw new IllegalArgumentException("이미 사용 중인 username입니다: " + dto.username());
        }

        boolean emailExists = userData.values().stream()
                .anyMatch(u -> u.getEmail().equals(dto.email()));
        if (emailExists) {
            throw new IllegalArgumentException("이미 사용 중인 email입니다: " + dto.email());
        }

        // 2. 프로필 이미지가 있는 경우 BinaryContent 저장
        UUID profileImageId = null;
        if (dto.profileImage() != null) {
            BinaryContentCreateDto imgDto = dto.profileImage();
            BinaryContent profile = new BinaryContent(
                    imgDto.fileName(),
                    (long) imgDto.bytes().length,
                    imgDto.contentType(),
                    imgDto.bytes()
            );
            binaryContentData.put(profile.getId(), profile);
            profileImageId = profile.getId();
        }

        // 3. User 생성 및 저장
        User user = new User(dto.username(), dto.email(), dto.password(), profileImageId);
        userData.put(user.getId(), user);

        // 4. UserStatus 생성 및 저장 (기본 접속 시간: 현재 시각)
        UserStatus status = new UserStatus(user.getId(), Instant.now());
        userStatusData.put(status.getId(), status);

        return UserResponseDto.of(user, status.isOnline());
    }

    @Override
    public UserResponseDto find(UUID id) {
        User user = userData.get(id);
        if (user == null) {
            throw new NoSuchElementException("유저를 찾을 수 없습니다: " + id);
        }
        return UserResponseDto.of(user, isUserOnline(user.getId()));
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userData.values().stream()
                .map(user -> UserResponseDto.of(user, isUserOnline(user.getId())))
                .toList();
    }

    @Override
    public UserResponseDto update(UserUpdateDto dto) {
        User user = userData.get(dto.id());
        if (user == null) {
            throw new NoSuchElementException("유저를 찾을 수 없습니다: " + dto.id());
        }

        UUID profileImageId = user.getProfileImageId();

        // 새 프로필 이미지가 등록된 경우 기존 것 제거 후 대체
        if (dto.newProfileImage() != null) {
            if (profileImageId != null) {
                binaryContentData.remove(profileImageId);
            }
            BinaryContentCreateDto newImgDto = dto.newProfileImage();
            BinaryContent newProfile = new BinaryContent(
                    newImgDto.fileName(),
                    (long) newImgDto.bytes().length,
                    newImgDto.contentType(),
                    newImgDto.bytes()
            );
            binaryContentData.put(newProfile.getId(), newProfile);
            profileImageId = newProfile.getId();
        }

        user.update(dto.username(), dto.email(), dto.password(), profileImageId);

        return UserResponseDto.of(user, isUserOnline(user.getId()));
    }

    @Override
    public void delete(UUID id) {
        User user = userData.remove(id);
        if (user == null) {
            throw new NoSuchElementException("유저를 찾을 수 없습니다: " + id);
        }

        // 1. 프로필 이미지(BinaryContent) 삭제
        if (user.getProfileImageId() != null) {
            binaryContentData.remove(user.getProfileImageId());
        }

        // 2. 연관 UserStatus 삭제
        userStatusData.values().removeIf(status -> status.getUserId().equals(id));
    }

    private boolean isUserOnline(UUID userId) {
        return userStatusData.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .findFirst()
                .map(UserStatus::isOnline)
                .orElse(false);
    }
}
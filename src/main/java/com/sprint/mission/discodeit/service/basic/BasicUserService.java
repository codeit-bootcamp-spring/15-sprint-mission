package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
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

    private static final long RECENT_ACTIVITY_SECONDS = 5 * 60;

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserResponse create(UserCreateRequest request, BinaryContentCreateRequest profileRequest) {
        if (userRepository.existsByUserName(request.userName())) {
            throw new IllegalStateException("이미 존재하는 userName: " + request.userName());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("이미 존재하는 email: " + request.email());
        }

        UUID profileId = null;
        if (profileRequest != null) {
            BinaryContent profile = new BinaryContent(
                    profileRequest.fileName(),
                    profileRequest.fileSize(),
                    profileRequest.fileType(),
                    profileRequest.bytes()
            );
            profileId = binaryContentRepository.save(profile).getId();
        }

        User user = new User(request.userName(), request.email(), request.password(), profileId);
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
        userStatusRepository.save(userStatus);

        return toResponse(user);
    }

    @Override
    public UserResponse read(UUID userId) {
        User user = userRepository.read(userId);
        if (user == null) {
            throw new NoSuchElementException("존재하지 않는 유저");
        }
        return toResponse(user);
    }

    @Override
    public List<UserResponse> readAll() {
        return userRepository.readAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public UserResponse update(UUID userId, UserUpdateRequest request, BinaryContentCreateRequest profileRequest) {
        User user = userRepository.read(userId);
        if (user == null) {
            throw new NoSuchElementException("존재하지 않는 유저");
        }

        UUID profileId = user.getProfileId();
        if (profileRequest != null) {
            BinaryContent profile = new BinaryContent(
                    profileRequest.fileName(),
                    profileRequest.fileSize(),
                    profileRequest.fileType(),
                    profileRequest.bytes()
            );
            profileId = binaryContentRepository.save(profile).getId();
        }

        user.update(request.userName(), request.email(), request.password(), profileId);
        userRepository.save(user);

        return toResponse(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.read(userId);
        if (user == null) {
            throw new NoSuchElementException("존재하지 않는 유저");
        }

        if (user.getProfileId() != null) {
            binaryContentRepository.delete(user.getProfileId());
        }

        userStatusRepository.deleteByUserId(userId);
        userRepository.delete(userId);
    }

    private UserResponse toResponse(User user) {
        UserStatus userStatus = userStatusRepository.readByUserId(user.getId());
        boolean online = isOnline(userStatus);

        return new UserResponse(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getProfileId(),
                online,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    // 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 온라인으로 판정
    private boolean isOnline(UserStatus userStatus) {
        return userStatus != null
                && userStatus.getLastAccessedAt() != null
                && userStatus.getLastAccessedAt().isAfter(Instant.now().minusSeconds(RECENT_ACTIVITY_SECONDS));
    }
}
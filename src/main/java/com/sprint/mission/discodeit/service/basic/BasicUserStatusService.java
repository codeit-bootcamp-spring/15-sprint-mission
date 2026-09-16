package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusDto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusDto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateRequest request) {

        // 해당 User가 실제 존재하는지 확인
        if (!userRepository.existsById(request.userId())) {
            throw new IllegalArgumentException(
                    "존재하지 않는 사용자입니다."
            );
        }

        // 같은 User의 UserStatus가 이미 있는지 확인
        if (userStatusRepository
                .findByUserId(request.userId())
                .isPresent()) {

            throw new IllegalArgumentException(
                    "이미 UserStatus가 존재합니다."
            );
        }

        UserStatus userStatus =
                new UserStatus(request.userId());

        userStatusRepository.save(userStatus);

        return userStatus;
    }

    @Override
    public UserStatus find(UUID id) {

        return userStatusRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public List<UserStatus> findAll() {

        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(
            UUID id,
            UserStatusUpdateRequest request
    ) {

        UserStatus userStatus =
                userStatusRepository
                        .findById(id)
                        .orElse(null);

        if (userStatus == null) {
            return null;
        }

        userStatus.updateLastActiveAt(
                request.lastActiveAt()
        );

        userStatusRepository.save(userStatus);

        return userStatus;
    }

    @Override
    public UserStatus updateByUserId(
            UUID userId,
            UserStatusUpdateRequest request
    ) {

        UserStatus userStatus =
                userStatusRepository
                        .findByUserId(userId)
                        .orElse(null);

        if (userStatus == null) {
            return null;
        }

        userStatus.updateLastActiveAt(
                request.lastActiveAt()
        );

        userStatusRepository.save(userStatus);

        return userStatus;
    }

    @Override
    public void delete(UUID id) {

        userStatusRepository.deleteById(id);
    }
}
package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateRequest request) {
        if (userRepository.read(request.userId()) == null) {
            throw new NoSuchElementException("존재하지 않는 유저");
        }

        if (userStatusRepository.existsByUserId(request.userId())) {
            throw new IllegalStateException("이미 존재하는 데이터");
        }

        UserStatus userStatus = new UserStatus(request.userId(), request.lastAccessedAt());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus read(UUID id) {
        return userStatusRepository.read(id);
    }

    @Override
    public List<UserStatus> readAll() {
        return userStatusRepository.readAll();
    }

    @Override
    public UserStatus update(UUID id, UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.read(id);
        userStatus.update(request.lastAccessedAt());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.readByUserId(userId);
        userStatus.update(request.lastAccessedAt());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.delete(id);
    }
}
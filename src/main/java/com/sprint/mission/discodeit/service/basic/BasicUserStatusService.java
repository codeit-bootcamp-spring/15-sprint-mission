package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusDto create(UserStatusCreateRequest request) {

        userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 사용자입니다. ID: " + request.userId()
                        )
                );

        if (userStatusRepository.findByUserId(request.userId()).isPresent()) {
            throw new IllegalArgumentException(
                    "이미 존재하는 사용자 상태입니다."
            );
        }

        UserStatus userStatus = new UserStatus(
                request.userId(),
                request.lastActiveAt()
        );

        UserStatus savedUserStatus = userStatusRepository.save(userStatus);

        return new UserStatusDto(
                savedUserStatus.getId(),
                savedUserStatus.getUserId(),
                savedUserStatus.getLastActiveAt(),
                savedUserStatus.isOnline(),
                savedUserStatus.getCreatedAt(),
                savedUserStatus.getUpdatedAt()
        );
    }

    @Override
    public UserStatusDto find(UUID id) {

        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 사용자 상태입니다. ID: " + id
                        )
                );

        return new UserStatusDto(
                userStatus.getId(),
                userStatus.getUserId(),
                userStatus.getLastActiveAt(),
                userStatus.isOnline(),
                userStatus.getCreatedAt(),
                userStatus.getUpdatedAt()
        );
    }

    @Override
    public List<UserStatusDto> findAll() {

        return userStatusRepository.findAll()
                .stream()
                .map(userStatus -> new UserStatusDto(
                        userStatus.getId(),
                        userStatus.getUserId(),
                        userStatus.getLastActiveAt(),
                        userStatus.isOnline(),
                        userStatus.getCreatedAt(),
                        userStatus.getUpdatedAt()
                ))
                .toList();
    }

    @Override
    public UserStatusDto update(UserStatusUpdateRequest request) {

        UserStatus userStatus = userStatusRepository.findById(request.id())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 사용자 상태입니다. ID: " + request.id()
                        )
                );

        userStatus.update(request.lastActiveAt());

        UserStatus savedUserStatus = userStatusRepository.save(userStatus);

        return new UserStatusDto(
                savedUserStatus.getId(),
                savedUserStatus.getUserId(),
                savedUserStatus.getLastActiveAt(),
                savedUserStatus.isOnline(),
                savedUserStatus.getCreatedAt(),
                savedUserStatus.getUpdatedAt()
        );
    }

    @Override
    public UserStatusDto updateByUserId(UUID userId, Instant lastActiveAt) {

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 사용자 상태입니다. User ID: " + userId
                        )
                );

        userStatus.update(lastActiveAt);

        UserStatus savedUserStatus = userStatusRepository.save(userStatus);

        return new UserStatusDto(
                savedUserStatus.getId(),
                savedUserStatus.getUserId(),
                savedUserStatus.getLastActiveAt(),
                savedUserStatus.isOnline(),
                savedUserStatus.getCreatedAt(),
                savedUserStatus.getUpdatedAt()
        );
    }

    @Override
    public void delete(UUID id) {

        userStatusRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 사용자 상태입니다. ID: " + id
                        )
                );

        userStatusRepository.deleteById(id);
    }

}


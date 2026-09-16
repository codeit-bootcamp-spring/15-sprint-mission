package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
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
    public UserDto create(UserCreateRequest request) {

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new IllegalArgumentException(
                    "이미 존재하는 username입니다: " + request.username()
            );
        }

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException(
                    "이미 존재하는 email입니다: " + request.email()
            );
        }

        UUID profileId = null;

        if (request.profile() != null) {
            BinaryContentCreateRequest profileRequest = request.profile();

            BinaryContent profile = new BinaryContent(
                    profileRequest.fileName(),
                    profileRequest.contentType(),
                    profileRequest.bytes()
            );

            profileId = binaryContentRepository.save(profile).getId();
        }

        User user = new User(
                request.username(),
                request.email(),
                request.password(),
                profileId
        );

        User savedUser = userRepository.save(user);

        UserStatus userStatus = new UserStatus(
                savedUser.getId(),
                Instant.now()
        );

        UserStatus savedUserStatus = userStatusRepository.save(userStatus);

        return new UserDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getProfileId(),
                savedUserStatus.isOnline(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt()
        );
    }

    @Override
    public UserDto find(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 사용자입니다. ID: " + id
                        )
                );

        UserStatus userStatus = userStatusRepository.findByUserId(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "사용자 상태가 존재하지 않습니다. User ID: " + id
                        )
                );

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId(),
                userStatus.isOnline(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Override
    public List<UserDto> findAll() {

        return userRepository.findAll().stream()
                .map(user -> {

                    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                            .orElseThrow(() ->
                                    new NoSuchElementException(
                                            "사용자 상태가 존재하지 않습니다. User ID: " + user.getId()
                                    )
                            );

                    return new UserDto(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getProfileId(),
                            userStatus.isOnline(),
                            user.getCreatedAt(),
                            user.getUpdatedAt()
                    );
                })
                .toList();
    }

    @Override
    public UserDto update(UserUpdateRequest request) {

        User user = userRepository.findById(request.id())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 사용자입니다. ID: " + request.id()
                        )
                );

        userRepository.findByUsername(request.username())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(request.id())) {
                        throw new IllegalArgumentException(
                                "이미 존재하는 username입니다: " + request.username()
                        );
                    }
                });

        userRepository.findByEmail(request.email())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(request.id())) {
                        throw new IllegalArgumentException(
                                "이미 존재하는 email입니다: " + request.email()
                        );
                    }
                });

        UUID profileId = user.getProfileId();

        if (request.profile() != null) {

            if (profileId != null) {
                binaryContentRepository.deleteById(profileId);
            }

            BinaryContentCreateRequest profileRequest = request.profile();

            BinaryContent newProfile = new BinaryContent(
                    profileRequest.fileName(),
                    profileRequest.contentType(),
                    profileRequest.bytes()
            );

            profileId = binaryContentRepository.save(newProfile).getId();
        }

        user.update(
                request.username(),
                request.email(),
                request.password(),
                profileId
        );

        User savedUser = userRepository.save(user);

        UserStatus userStatus = userStatusRepository.findByUserId(savedUser.getId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "사용자 상태가 존재하지 않습니다. User ID: " + savedUser.getId()
                        )
                );

        return new UserDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getProfileId(),
                userStatus.isOnline(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt()
        );
    }
    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 사용자입니다. ID: " + id )
                );
        if (user.getProfileId() != null) {
            binaryContentRepository.deleteById(user.getProfileId());
        }

        UserStatus userStatus = userStatusRepository.findByUserId(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "사용자 상태가 존재하지 않습니다. User ID: " + id )
                );

        userStatusRepository.deleteById(userStatus.getId());

        userRepository.deleteById(id);
    }
}



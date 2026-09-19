package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.UserFindResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User create(UserCreateRequest userCreateRequest) {

        for (User user : userRepository.findAll()) {
            if (user.getName().equals(userCreateRequest.name())) {
                throw new IllegalArgumentException("중복된 이름입니다" + userCreateRequest.name());
            }

            if (user.getEmail().equals(userCreateRequest.email())) {
                throw new IllegalArgumentException("중복된 메일입니다" + userCreateRequest.email());
            }
        }

        validateEmail(userCreateRequest.email());

        UUID profileId = userCreateRequest.profileId().orElse(null);

        User user = new User(userCreateRequest.email(),userCreateRequest.password(),userCreateRequest.name(),userCreateRequest.nitroLevel(),profileId);
        UserStatus userStatus = new UserStatus(user.getId());
        userRepository.save(user);
        userStatusRepository.save(userStatus);

        return user;
    }

    @Override
    public UserFindResponse find(UUID id) {
        return userRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("유저 id 없음 : " + id));
    }

    @Override
    public List<UserFindResponse> findAll() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public User update(UUID id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("유저 id 없음 : " + id));



        for (User entry : userRepository.findAll()) {
            if (entry.getId().equals(id)) continue;

            if (entry.getName().equals(userUpdateRequest.name())) {

                throw new IllegalArgumentException("중복된 이름입니다" + userUpdateRequest.name());

            }
            if (entry.getEmail().equals(userUpdateRequest.email())) {
                throw new IllegalArgumentException("중복된 메일입니다" + userUpdateRequest.email());
            }
        }
        validateEmail(userUpdateRequest.email());

        UUID profileId = userUpdateRequest.profileId().orElse(null);
        user.update(userUpdateRequest.email(), userUpdateRequest.password(), userUpdateRequest.name(), userUpdateRequest.nitroLevel(),profileId);
        return userRepository.save(user);

    }

    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("유저 id 없음 : " + id));

        userStatusRepository.deleteByUserId(id);

        if (user.getProfileId() != null) {
            binaryContentRepository.deleteById(user.getProfileId());
        }

        userRepository.deleteById(id);
    }

    private UserFindResponse toDto(User user) {
        UserStatus userStatus = userStatusRepository
                .findByUserId(user.getId()).orElseThrow(() -> new NoSuchElementException("해당 유저의 스테이터스가 없습니다."));
        boolean online = userStatus.isOnline();

        //UUID testId= Optional.ofNullable(user.getProfileId()).orElse(null);

        return new UserFindResponse(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getEmail(),
                user.getName(),
                user.getNitroLevel(),
                Optional.ofNullable(user.getProfileId()),
                online
        );
    }

    private void validateEmail(String email){
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("메일 형식이 아님.");
        }
    }
}


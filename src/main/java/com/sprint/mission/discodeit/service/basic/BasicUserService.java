package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.UserReadResponse;
import com.sprint.mission.discodeit.entity.NitroLevel;
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
/*
    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }*/
    @Override
    public User create(UserCreateRequest userCreateRequest) {
        /*if (userRepository.findAll().stream().anyMatch(user -> user.getName().equals(userCreateRequest.name()))){
            throw new IllegalArgumentException("중복된 이름입니다" + userCreateRequest.name());
        }
        if (userRepository.findAll().stream().anyMatch(user -> user.getEmail().equals(userCreateRequest.email()))){
            throw new IllegalArgumentException("중복된 메일입니다" + userCreateRequest.email());
        }*/
        for (User user : userRepository.findAll()) {
            if (user.getName().equals(userCreateRequest.name())) {
                throw new IllegalArgumentException("중복된 이름입니다" + userCreateRequest.name());
            }

            if (user.getEmail().equals(userCreateRequest.email())) {
                throw new IllegalArgumentException("중복된 메일입니다" + userCreateRequest.email());
            }
        }
        UUID profileId = userCreateRequest.profileId().orElse(null);

        User user = new User(userCreateRequest.email(),userCreateRequest.password(),userCreateRequest.name(),userCreateRequest.nitroLevel(),profileId);
        UserStatus userStatus = new UserStatus(user.getId());
        userRepository.save(user);
        userStatusRepository.save(userStatus);

        return user;
    }

    @Override
    public UserReadResponse read(UUID id) {
        return userRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("유저 id 없음 : " + id));
    }

    @Override
    public List<UserReadResponse> readAll() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public User update(UUID id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("유저 id 없음 : " + id));

        for (User entry : userRepository.findAll()) {
            if (entry.getName().equals(userUpdateRequest.name())) {
                if(user.getName().equals(userUpdateRequest.name())){
                    System.out.println("기존 이름 그대로");
                }
                else{
                    throw new IllegalArgumentException("중복된 이름입니다" + userUpdateRequest.name());
                }
            }

            if (entry.getEmail().equals(userUpdateRequest.email())) {
                if(user.getEmail().equals(userUpdateRequest.email())){
                    System.out.println("기존 메일 그대로");
                }
                else{
                    throw new IllegalArgumentException("중복된 메일입니다" + userUpdateRequest.email());
                }

            }
        }
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

    private UserReadResponse toDto(User user) {
        boolean online = userStatusRepository.findByUserId(user.getId())
                .isOnline();

        //UUID testId= Optional.ofNullable(user.getProfileId()).orElse(null);

        return new UserReadResponse(
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
}


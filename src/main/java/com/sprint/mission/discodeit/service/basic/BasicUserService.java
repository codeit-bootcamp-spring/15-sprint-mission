package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.UserResponse;
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

    @Override
    public User create(UserCreateRequest userCreateRequest) {

        for (User user : userRepository.findAll()) {
            if (user.getUsername().equals(userCreateRequest.name())) {
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
    public UserResponse find(UUID id) {
        return userRepository.findById(id).map(this::toUserResponse)
                .orElseThrow(() -> new NoSuchElementException("유저 id 없음 : " + id));
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::toUserResponse).toList();
    }

    @Override
    public User update(UUID id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("유저 id 없음 : " + id));



        for (User entry : userRepository.findAll()) {
            if (entry.getId().equals(id)) continue;

            if (entry.getUsername().equals(userUpdateRequest.name())) {

                throw new IllegalArgumentException("중복된 이름입니다" + userUpdateRequest.name());

            }
            if (entry.getEmail().equals(userUpdateRequest.email())) {
                throw new IllegalArgumentException("중복된 메일입니다" + userUpdateRequest.email());
            }
        }
        validateEmail(userUpdateRequest.email());
        String email;
        String password;
        String name;
        NitroLevel nitroLevel;


        if(userUpdateRequest.email() != null) {
            email = userUpdateRequest.email();
        }else {
            email = user.getEmail();
        }

        if (userUpdateRequest.password() != null) {
            password = userUpdateRequest.password();
        } else {
            password = user.getPassword();
        }

        if(userUpdateRequest.name() != null) {
            name = userUpdateRequest.name();
        }else {
            name = user.getUsername();
        }

        if(userUpdateRequest.nitroLevel() != null) {
            nitroLevel = userUpdateRequest.nitroLevel();
        }else {
            nitroLevel = user.getNitroLevel();
        }





        UUID profileId = userUpdateRequest.profileId().orElse(user.getProfileId());
        user.update(email, password, name, nitroLevel, profileId);
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

    public UserResponse toUserResponse(User user) {
        UserStatus userStatus = userStatusRepository
                .findByUserId(user.getId()).orElseThrow(() -> new NoSuchElementException("해당 유저의 스테이터스가 없습니다."));
        boolean online = userStatus.isOnline();

        //UUID testId= Optional.ofNullable(user.getProfileId()).orElse(null);

        return new UserResponse(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getEmail(),
                user.getUsername(),
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


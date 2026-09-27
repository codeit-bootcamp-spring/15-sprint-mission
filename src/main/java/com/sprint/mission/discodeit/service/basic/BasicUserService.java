package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
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
    public User create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> binaryContentCreateRequest) {

        for (User user : userRepository.findAll()) {
            if (user.getUsername().equals(userCreateRequest.username())) {
                throw new IllegalArgumentException("중복된 이름입니다" + userCreateRequest.username());
            }

            if (user.getEmail().equals(userCreateRequest.email())) {
                throw new IllegalArgumentException("중복된 메일입니다" + userCreateRequest.email());
            }
        }
        validateEmail(userCreateRequest.email());

        //바이너리
        UUID profileId = binaryContentCreateRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length, contentType, bytes);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);

        User user = new User(userCreateRequest.email(),userCreateRequest.password(),userCreateRequest.username(),profileId);
        UserStatus userStatus = new UserStatus(user.getId());
        userRepository.save(user);
        userStatusRepository.save(userStatus);

        return user;
    }

    @Override
    public UserDto find(UUID id) {
        return userRepository.findById(id).map(this::toUserResponse)
                .orElseThrow(() -> new NoSuchElementException("유저 id 없음 : " + id));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(this::toUserResponse).toList();
    }

    @Override
    public User update(UUID id, UserUpdateRequest userUpdateRequest) {

        if (userUpdateRequest == null) {
            return userRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("유저 id 없음 : " + id));
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("유저 id 없음 : " + id));



        for (User entry : userRepository.findAll()) {
            if (entry.getId().equals(id)) continue;

            if (userUpdateRequest.name() != null
                    && entry.getUsername().equals(userUpdateRequest.name())) {

                throw new IllegalArgumentException("중복된 이름입니다" + userUpdateRequest.name());

            }
            if (userUpdateRequest.email() != null
            && entry.getEmail().equals(userUpdateRequest.email())) {
                throw new IllegalArgumentException("중복된 메일입니다" + userUpdateRequest.email());
            }
        }

        String email;
        String password;
        String name;


        if(userUpdateRequest.email() != null) {
            email = userUpdateRequest.email();
        }else {
            email = user.getEmail();
        }
        validateEmail(email);


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



        UUID profileId = userUpdateRequest.profileId().orElse(user.getProfileId());
        user.update(email, password, name, profileId);
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

    public UserDto toUserResponse(User user) {
        UserStatus userStatus = userStatusRepository
                .findByUserId(user.getId()).orElseThrow(() -> new NoSuchElementException("해당 유저의 스테이터스가 없습니다."));
        boolean online = userStatus.isOnline();

        //UUID testId= Optional.ofNullable(user.getProfileId()).orElse(null);

        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getEmail(),
                user.getUsername(),
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


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
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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

    @Transactional
    @Override
    public UserDto create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> binaryContentCreateRequest) {

        if(userRepository.existsByUsername(userCreateRequest.username())){
            throw new IllegalArgumentException("중복된 이름입니다" + userCreateRequest.username());
        }

        if(userRepository.existsByEmail(userCreateRequest.email())){
            throw new IllegalArgumentException("중복된 메일입니다" + userCreateRequest.email());
        }



        validateEmail(userCreateRequest.email());

        //바이너리
        BinaryContent profile = binaryContentCreateRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length, contentType, bytes);
                    return binaryContentRepository.save(binaryContent);
                })
                .orElse(null);

        User user = new User(userCreateRequest.email(),userCreateRequest.password(),userCreateRequest.username(),profile);
        UserStatus userStatus = new UserStatus(user, Instant.now());
        userRepository.save(user);
        userStatusRepository.save(userStatus);

        return UserDto.from(user);
    }



    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll() {

        return userRepository.findAll().stream().map(UserDto::from).toList();
    }

    @Transactional
    @Override
    public UserDto update(UUID id, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> binaryContentCreateRequest) {



        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("유저 id 없음 : " + id));

        if (userUpdateRequest.newUsername() != null
                && userRepository.existsByUsernameAndIdNot(
                userUpdateRequest.newUsername(), id)) {
            throw new IllegalArgumentException(
                    "중복된 이름입니다: " + userUpdateRequest.newUsername());
        }

        if (userUpdateRequest.newEmail() != null
                && userRepository.existsByEmailAndIdNot(
                userUpdateRequest.newEmail(), id)) {
            throw new IllegalArgumentException(
                    "중복된 메일입니다: " + userUpdateRequest.newEmail());
        }

        String email;
        String password;
        String name;


        email = userUpdateRequest.newEmail() == null ? user.getEmail() : userUpdateRequest.newEmail();
        validateEmail(email);

        password = userUpdateRequest.newPassword() == null ? user.getPassword() : userUpdateRequest.newPassword();
        name = userUpdateRequest.newUsername() == null ? user.getUsername() : userUpdateRequest.newUsername();



        BinaryContent profile = binaryContentCreateRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length, contentType, bytes);
                    return binaryContentRepository.save(binaryContent);
                })
                .orElse(user.getProfile());


        user.update(email, password, name, profile);
        return UserDto.from(user);

    }

    @Transactional
    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("유저 id 없음 : " + id));

        userRepository.delete(user);
    }



    private void validateEmail(String email){
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

            throw new IllegalArgumentException("메일 형식이 아님.");
        }
    }
}


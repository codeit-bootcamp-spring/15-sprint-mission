package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateUserEmailException;
import com.sprint.mission.discodeit.exception.DuplicateUserNameException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> binaryContentCreateRequest) {

        if(userRepository.existsByUsername(userCreateRequest.username())){
            throw new DuplicateUserNameException(userCreateRequest.username());
        }

        if(userRepository.existsByEmail(userCreateRequest.email())){
            throw new DuplicateUserEmailException(userCreateRequest.email());
        }


        //바이너리
        BinaryContent profile = binaryContentCreateRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length, contentType);
                    BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
                    binaryContentStorage.put(createdBinaryContent.getId(),bytes);
                    return createdBinaryContent;
                })
                .orElse(null);

        User user = new User(userCreateRequest.email(),userCreateRequest.password(),userCreateRequest.username(),profile);
        UserStatus userStatus = new UserStatus(user, Instant.now());
        userRepository.save(user);
        userStatusRepository.save(userStatus);

        UserDto userDto = userMapper.toDto(user);
        log.info("회원가입 완료: userId={}", user.getId());
        return userDto;
    }



    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll() {
        List<UserDto> dtos = userRepository.findAllJoinProfileAndStatus().stream().map(userMapper::toDto).toList();
        log.info("전체 회원 조회 완료 count : {}", dtos.size());
        return dtos;
    }

    @Transactional
    @Override
    public UserDto update(UUID id, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> binaryContentCreateRequest) {



        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (userUpdateRequest.newUsername() != null
                && userRepository.existsByUsernameAndIdNot(
                userUpdateRequest.newUsername(), id)) {
            throw new DuplicateUserNameException(
                    userUpdateRequest.newUsername());
        }

        if (userUpdateRequest.newEmail() != null
                && userRepository.existsByEmailAndIdNot(
                userUpdateRequest.newEmail(), id)) {
            throw new DuplicateUserEmailException(
                    userUpdateRequest.newEmail());
        }

        String email;
        String password;
        String name;


        email = userUpdateRequest.newEmail() == null ? user.getEmail() : userUpdateRequest.newEmail();


        password = userUpdateRequest.newPassword() == null ? user.getPassword() : userUpdateRequest.newPassword();
        name = userUpdateRequest.newUsername() == null ? user.getUsername() : userUpdateRequest.newUsername();



        BinaryContent profile = binaryContentCreateRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length, contentType);
                    BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
                    binaryContentStorage.put(createdBinaryContent.getId(),bytes);
                    return createdBinaryContent;
                })
                .orElse(user.getProfile());


        user.update(email, password, name, profile);
        UserDto userDto = userMapper.toDto(user);
        log.info("회원정보 수정 완료: userId={}", id);
        return userDto;

    }

    @Transactional
    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);
        log.info("회원탈퇴 완료 id : {}", id);
    }




}


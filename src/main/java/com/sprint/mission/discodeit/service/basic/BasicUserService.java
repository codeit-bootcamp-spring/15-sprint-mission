package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDto.UserFindResponse;
import com.sprint.mission.discodeit.dto.UserDto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.DuplicateUserException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.dto.BinaryContentRequest.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User create(
            UserCreateRequest userRequest,
            BinaryContentCreateRequest profileRequest
    ) {

        // ① 기존 사용자 가져오기
        List<User> users = userRepository.findAll();

        // ② 이름과 이메일 중복 확인
        for (User user : users) {

            if (user.getName().equals(userRequest.name())) {
                throw new DuplicateUserException("이미 사용 중인 이름입니다.");
            }

            if (user.getEmail().equals(userRequest.email())) {
                throw new DuplicateUserException("이미 사용 중인 이메일입니다.");
            }
        }

        // ③ 중복이 없으면 기존처럼 User 생성
        User user = new User(
                userRequest.name(),
                userRequest.email(),
                userRequest.password()
        );

        // ④ 선택적으로 프로필 이미지 등록
        if (profileRequest != null) {

            BinaryContent profile = new BinaryContent(
                    profileRequest.contentType(),
                    profileRequest.fileName(),
                    profileRequest.fileSize(),
                    profileRequest.content()
            );
            binaryContentRepository.save(profile);
            user.setProfileId(profile.getId());
        }

        userRepository.save(user);
        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);

        return user;
    }

    @Override
    public UserFindResponse find(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "존재하지 않는 사용자입니다."
                        )
                );

        UserStatus status =
                userStatusRepository.findByUserId(id).orElse(null);

        boolean online = status != null && status.isOnline();

        return new UserFindResponse(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getName(),
                user.getEmail(),
                user.getProfileId(),
                online
        );
    }

    @Override
    public List<UserFindResponse> findAll() {

        // 1. 사용자 전체 조회
        List<User> users = userRepository.findAll();

        // 2. 사용자 상태 전체 조회
        List<UserStatus> statuses = userStatusRepository.findAll();

        // 3. userId를 기준으로 UserStatus를 Map에 저장
        Map<UUID, UserStatus> statusMap = new HashMap<>();

        for (UserStatus status : statuses) {
            statusMap.put(status.getUserId(), status);
        }

        // 4. 반환할 DTO 목록 생성
        List<UserFindResponse> result = new ArrayList<>();

        // 5. 사용자별 상태를 Map에서 찾아 DTO 생성
        for (User user : users) {

            UserStatus status = statusMap.get(user.getId());

            boolean online = status != null && status.isOnline();

            result.add(new UserFindResponse(
                    user.getId(),
                    user.getCreatedAt(),
                    user.getUpdatedAt(),
                    user.getName(),
                    user.getEmail(),
                    user.getProfileId(),
                    online
            ));
        }

        return result;
    }

    @Override
    public User update(
            UUID id,
            UserUpdateRequest userRequest,
            BinaryContentCreateRequest profileRequest
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "수정할 사용자를 찾을 수 없습니다."
                        )
                );

        // 1. 먼저 중복 검사
        for (User other : userRepository.findAll()) {

            if (other.getId().equals(id)) {
                continue;
            }

            if (userRequest.name() != null
                    && other.getName().equals(userRequest.name())) {
                throw new DuplicateUserException("이미 사용 중인 이름입니다.");
            }

            if (userRequest.email() != null
                    && other.getEmail().equals(userRequest.email())) {
                throw new DuplicateUserException("이미 사용 중인 이메일입니다.");
            }
        }

        // 2. null이 아닌 값만 수정
        if (userRequest.name() != null) {
            user.setName(userRequest.name());
        }

        if (userRequest.email() != null) {
            user.setEmail(userRequest.email());
        }

        if (userRequest.password() != null) {
            user.setPassword(userRequest.password());
        }

        // 3. 프로필 이미지가 들어왔을 때만 교체
        if (profileRequest != null) {

            if (user.getProfileId() != null) {
                binaryContentRepository.deleteById(user.getProfileId());
            }

            BinaryContent newProfile = new BinaryContent(
                    profileRequest.contentType(),
                    profileRequest.fileName(),
                    profileRequest.fileSize(),
                    profileRequest.content()
            );

            binaryContentRepository.save(newProfile);
            user.setProfileId(newProfile.getId());
        }

        user.setUpdatedAt();
        userRepository.save(user);

        return user;
    }

    @Override
    public void delete(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "삭제할 사용자를 찾을 수 없습니다."
                        )
                );

        if (user.getProfileId() != null) {
            binaryContentRepository.deleteById(user.getProfileId());
        }
        userStatusRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }
}
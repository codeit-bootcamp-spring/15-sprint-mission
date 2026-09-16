package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UUID userId) {
        if (userRepository.find(userId) == null) throw new IllegalArgumentException("user가 없음");

        UserStatus userStatus = new UserStatus(userId);
        // 중복 검사. 안찾아지는 경우에만 저장.
        if (userStatusRepository.findByUserId(userId) == null) {
            userStatusRepository.save(userStatus);

            return userStatus;
        }
        throw new IllegalArgumentException("잘못된 userId 입력");
    }

    @Override
    public UserStatus find(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.find(userStatusId);
        if (userStatus != null) {
            return userStatus;
        }
        throw new IllegalArgumentException("찾을 수 없음");
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public void update(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.find(userStatusId);
        if (userStatus == null) {
            throw new IllegalArgumentException("찾을 수 없음");
        }

        userStatus.updateOnlineAt();
        userStatus.autoSetUpdatedAt();

        userStatusRepository.delete(userStatus.getId());
        userStatusRepository.save(userStatus);
    }

    @Override
    public void updateByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        if (userStatus == null) {
            throw new IllegalArgumentException("찾을 수 없음");
        }

        userStatus.updateOnlineAt();
        userStatus.autoSetUpdatedAt();

        userStatusRepository.delete(userStatus.getId());
        userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.find(userStatusId);
        if (userStatus == null) {
            throw new IllegalArgumentException("찾을 수 없음");
        }

        userStatusRepository.delete(userStatusId);
    }
}

package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserStatusMapper userStatusMapper;

    @Transactional
    @Override
    public UserStatusDto create(UserStatusCreateRequest userStatusCreateRequest) {
        UUID userId =userStatusCreateRequest.userId();

        User user = userRepository.findById(userStatusCreateRequest.userId()).
                orElseThrow(() ->  new NoSuchElementException("존재하지 않는 유저 id : " + userId));

        if(userStatusRepository.existsByUserId(userId)){
            throw new IllegalArgumentException("이미 해당 유저의 스테이터스가 존재합니다. user ID : "+userId);
        }

        UserStatus userStatus = new UserStatus(user,userStatusCreateRequest.lastActiveAt());
        userStatusRepository.save(userStatus);

        return userStatusMapper.toDto(userStatus);

    }


    @Transactional
    @Override
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId).
                orElseThrow(() -> new NoSuchElementException("유저의 스테이터스가 없음 userID : " + userId));

        userStatus.update(userStatusUpdateRequest.newLastActiveAt());
        return userStatusMapper.toDto(userStatus);
    }


}

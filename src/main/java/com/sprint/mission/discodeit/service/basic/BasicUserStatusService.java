package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    @Override
    public UserStatus create(UserStatusCreateRequest userStatusCreateRequest) {
        UUID userId =userStatusCreateRequest.userId();

        User user = userRepository.findById(userStatusCreateRequest.userId()).
                orElseThrow(() ->  new NoSuchElementException("존재하지 않는 유저 id : " + userId));

        if(userStatusRepository.existsByUserId(userId)){
            throw new IllegalArgumentException("이미 해당 유저의 스테이터스가 존재합니다. user ID : "+userId);
        }



        UserStatus userStatus = new UserStatus(user,userStatusCreateRequest.lastActiveAt());
        return userStatusRepository.save(userStatus);

    }



    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId).
                orElseThrow(() -> new NoSuchElementException("유저의 스테이터스가 없음 userID : " + userId));

        userStatus.update(userStatusUpdateRequest.newLastActiveAt());
        return userStatusRepository.save(userStatus);
    }


}

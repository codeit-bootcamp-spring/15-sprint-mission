package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private UserRepository userRepository;
    private UserStatusRepository userStatusRepository;



    //User 저장해라 -> Repository에 요청
    @Override
    public UserDto create(UserCreateRequest request) {

        userRepository.findByUsername(request.getUsername()).ifPresent(user -> {
            throw new  IllegalArgumentException("이미 존재하지 않는 username입니다.");
        });

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new   IllegalArgumentException("이미 존재하는 email입니다.");
        });

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getProfileImageId()
        );

       userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);

        return new UserDto(user.getId(), user.getUsername());
    }


    //Repository에서 해당 ID의 User를 찾음. → 찾았다면 .map을 통해 를user userDto로 변환
    @Override
    public Optional<UserDto> findById(UUID id) {
        return userRepository.findById(id).map(user -> new UserDto(
                user.getId(), user.getUsername()
        ));
    }

    @Override
    public List<UserDto> findAll() {

        return userRepository.findAll().stream().map(
                user ->
                new UserDto(user.getId(),
                user.getUsername())).toList();
    }

    @Override
    public Optional<UserDto> update(UserUpdateRequest request) {
        return userRepository.findById(request.getId())
                .map(user -> {

                    user.update(
                            request.getUsername(),
                            request.getProfileImageId()
                    );

                    userRepository.save(user);

                    return new UserDto(
                            user.getId(),
                            user.getUsername()
                    );
                });



    }

    @Override
    public void delete(UUID id) {


    }
}




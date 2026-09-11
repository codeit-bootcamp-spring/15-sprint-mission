package com.sprint.mission.discodeit.Basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final FileUserRepository userRepository;


    //User 저장해라 -> Repository에 요청
    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User create(String username, String email, String password) throws IOException {
        User user = new User(username, email, password);
        return userRepository.save(user);
    }
    //Repository에게 해당 ID를 가진 User찾아달라고 요청하기.
    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> update(UUID id, String name) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }
        User user = optionalUser.get();
        user.update(name);
        userRepository.save(user);

        return Optional.of(user);

    }

    @Override
    public User create(String 길춘배) {
        return null;
    }


    @Override
    public List<User> findall() {
        return List.of();
    }

    @Override
    public void delete(UUID id) throws IOException {
        userRepository.delete(id);

    }


}



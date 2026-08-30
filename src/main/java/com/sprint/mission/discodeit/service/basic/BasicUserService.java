package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String userName, String email, String password) {
        User user = new User(userName, email, password);
        return userRepository.save(user);
    }

    @Override
    public User read(UUID userId) {
        return userRepository.read(userId);
    }

    @Override
    public List<User> readAll() {
        return userRepository.readAll();
    }

    @Override
    public User update(UUID userId, String userName, String email, String password) {
        User user = userRepository.read(userId);
        user.update(userName, email, password);
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        userRepository.delete(userId);
    }
}
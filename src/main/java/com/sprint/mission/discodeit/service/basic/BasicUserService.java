package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;

import java.util.*;

public class BasicUserService implements UserService{

    //private final UserRepository userRepository = new FileUserRepository();

    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(String userName) {
        User user = new User(userName); // 생성자랑 필드 맞춰서 쓰기
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UUID id, String userName) {
        return userRepository.update(id, userName);
    }

    @Override
    public User deleteUser(UUID id) {
        return userRepository.delete(id);
    }
}


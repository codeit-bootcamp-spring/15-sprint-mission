package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicUserService implements UserService {

    // Repository 인터페이스를 필드로 선언
    private final UserRepository userRepository;

    // 생성자를 통해 의존성 주입(DI)
    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String username, String email) {
        User user = new User(username, email);
        return userRepository.save(user); // 직접 저장하지 않고 Repository 활용
    }

    @Override
    public User find(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다. ID: " + id));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID id, String username, String email) {
        User user = find(id);
        user.update(username, email);
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID id) {
        find(id); // 존재 확인
        userRepository.deleteById(id);
    }
}
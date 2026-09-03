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
    public User createUser(String nickname) {
        User user = new User(nickname);
        return userRepository.createUser(user);
    }

    @Override
    public User getUser(UUID id) {
        return findUser(id);
    }

    @Override
    public String getUserNickname(UUID uuid) {
        return findUser(uuid).getNickName();
    }

    @Override
    public void updateUser(UUID id, String nickname) {
        User user = findUser(id);
        user.updateUser(nickname);
        // File 기반 Repository는 findById가 새로 역직렬화된 복사본을 반환하므로
        // 수정 후 반드시 다시 save 해야 변경사항이 실제로 반영된다.
        userRepository.createUser(user);
    }

    @Override
    public List<User> getUserAll() {
        return userRepository.getUserAll();
    }

    @Override
    public void userDelete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다. id = " + id);
        }
        userRepository.deleteUser(id);
    }

    private User findUser(UUID id) {
        return userRepository.getUser(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. id = " + id));
    }
}

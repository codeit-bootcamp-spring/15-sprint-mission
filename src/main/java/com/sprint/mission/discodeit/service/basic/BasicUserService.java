package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Set;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String name, String email, String id) {
        User user = new User(name, email, id);
        if (userRepository.readAll() != null) {
            for (User u : userRepository.readAll()) {
                if (u.getEmail().equals(email) && u.getUserId().equals(id)) {
                    System.out.println("중복된 이메일/아이디 입니다: " + u.getEmail() + " " + u.getUserId());
                    return null;
                }
            }
        }
        if (userRepository.create(user)) {
            return user;
        }

        System.out.println("알 수 없는 이유로 생성에 실패했습니다.");
        return null;
    }

    @Override
    public User read(String name) {
        Set<User> users = this.readAll();
        if (users.isEmpty()) {
            System.out.println("현재 저장된 user가 없습니다.");
            return null;
        }

        for (User user: users) {
            if (user.getUser().equals(name)) return user;
        }

        return null;
    }

    @Override
    public Set<User> readAll() {
        return userRepository.readAll();
    }

    @Override
    public void update(User user, String data, String email, String id) {
        if (user == null) {
            System.out.println("저장소에서 유저를 찾을 수 없습니다.");
            return;
        }

        user.setUser(data);
        user.setEmail(email);
        user.setUserId(id);
        user.autoSetUpdatedAt();

        if (userRepository.update(user)) {
            System.out.println("정상적으로 업데이트가 되었습니다.");
        }
        else {
            System.out.println("오류가 발생하여 업데이트가 되지 않았습니다.");
        }
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }
}

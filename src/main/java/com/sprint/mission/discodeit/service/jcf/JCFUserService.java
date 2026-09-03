package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService {

    private final Map<UUID, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public User create(String userName, String email, String password) {
        User user = new User(userName, email, password);
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User read(UUID userId) {
        return data.get(userId);
    }

    @Override
    public List<User> readAll() {
        return data.values().stream().toList();
    }

    @Override
    public User update(UUID userId, String userName, String email, String password) {
        User user = read(userId);
        user.update(userName, email, password);
        return user;
    }

    @Override
    public void delete(UUID userId) {
        data.remove(userId);

    }
}
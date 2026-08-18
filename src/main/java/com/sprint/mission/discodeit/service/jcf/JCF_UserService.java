package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCF_UserService implements UserService {

    private final Map<UUID, User> data;

    public JCF_UserService() {
        this.data = new HashMap<>();
    }

    @Override
    public User create(String name, String email, String password) {

        User user = new User(name, email, password);

        data.put(user.getId(), user);

        return user;
    }

    @Override
    public User read(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> readAll() {
        return List.of();
    }

    @Override
    public User update(UUID id, String name, String email, String password) {
        User user = data.get(id);

        if (user == null) {
            return null;
        }

        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}

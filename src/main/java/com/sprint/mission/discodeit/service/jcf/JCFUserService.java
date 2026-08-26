package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {

    private final List<User> data;

    public JCFUserService() {
        this.data = new ArrayList<>();
    }


    @Override
    public User create(User user) {
        data.add(user);
        return user;
    }

    @Override
    public User findById(UUID id) {
        for (User user : data) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return data;
    }

    @Override
    public User update(UUID id, String name) {
        User user = findById(id);
        if (user != null) {
            user.update(name);
        }
        return user;
    }

    @Override
    public void delete(UUID id) {
        User user = findById(id);
        if (user != null) {
            data.remove(user);
        }

    }
}

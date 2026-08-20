package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class JCFUserService implements UserService, Serializable {
    private final Set<User> data;

    public JCFUserService() {
            this.data = new HashSet<>();
    }

    @Override
    public void create(User user) {
        this.data.add(user);
    }

    @Override
    public User read(User user) {
        for (User u : this.data) {
            if (u.getId().equals(user.getId())) return u;
        }
        return null;
    }

    @Override
    public Set<User> readAll() {
        return this.data;
    }

    @Override
    public void update(User user, String data) {
        for (User u : this.data) {
            if (u.getId().equals(user.getId())) {
                u.setUser(data);
                u.setUpdatedAt();
            }
        }
        System.out.println("변경 완료");
    }

    @Override
    public void delete(User user) {
        this.data.remove(user);
    }
}

package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class JCFUserRepository implements UserRepository {
    private final Set<User> data;

    public JCFUserRepository() {
        this.data = new HashSet<>();
    }

    @Override
    public boolean create(User user) {
        try {
            this.data.add(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

/*    @Override
    public User read(String name) {
        for (User u : this.data) {
            if (u.getUser().equals(name)) {
                return u;
            }
        }
        return null;
    }*/

    @Override
    public Set<User> readAll() {
        return this.data.stream()
                .map(e -> (User) e)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean update(User user) {
        for (User u : this.data) {
            if (u.getId().equals(user.getId())) {
                u.setEmail(user.getEmail());
                u.setUser(user.getUser());
                u.setUserId(user.getUserId());
                u.setUpdatedAt(user.getUpdatedAt());

                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(User user) {
        for (User u : this.data) {
            if (u.getId().equals(user.getId())) {
                this.data.remove(u);
                return true;
            }
        }
        return false;
    }
}

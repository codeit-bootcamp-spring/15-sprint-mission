package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data;

    public JCFUserRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public User save(User user) {

        data.put(user.getId(), user);

        return user;
    }

    @Override
    public User findById(UUID id) {

        return data.get(id);
    }

    @Override
    public List<User> findAll() {

        return List.copyOf(data.values());
    }

    @Override
    public void deleteById(UUID id) {

        data.remove(id);
    }
}
package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data;

    public JCFUserRepository() {
        this.data = new LinkedHashMap<>();
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public User load(UUID id) {
        return data.get(id);
    }

    @Override
    public boolean delete(UUID id) {
        return data.remove(id, data.get(id));
    }
}

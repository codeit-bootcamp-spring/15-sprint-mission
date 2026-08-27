package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    final Map<UUID, User> data = new HashMap<>();

    private final static JCFUserRepository instance = new JCFUserRepository();
    private JCFUserRepository() { }
    public static JCFUserRepository getInstance() {
        return instance;
    }


    @Override
    public User save(User user) {
        this.data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void deleteById(UUID id) {
        this.data.remove(id);
    }
}

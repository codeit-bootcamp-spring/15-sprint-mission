package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true
)
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
    public Optional<User> findById(UUID id) {

        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> findAll() {

        return List.copyOf(data.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    @Override
    public Optional<User> findByName(String username) {
        return findAll().stream()
                .filter(user -> user.getName().equals(username))
                .findFirst();
    }
}
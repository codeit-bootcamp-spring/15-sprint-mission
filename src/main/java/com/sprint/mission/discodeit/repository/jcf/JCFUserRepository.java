package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public User save(User user)
    {
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id)
    {
        return  Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email)
    {
        return data.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> findAll()
    {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(UUID id)
    {
        data.remove(id);
    }
    @Override
    public boolean existsById(UUID id)
    {
        return data.containsKey(id);
    }
//추가된 인터페이스에 맞춰서 두가지 작성
    @Override
    public boolean existsByEmail(String email)
    {
        return data.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existsByUsername(String username) {
        return data.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return data.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
}

package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserStatusRepository implements UserStatusRepository {

    private final Map<UUID, UserStatus> data;

    public JCFUserStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        data.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public UserStatus read(UUID id) {
        return data.get(id);
    }

    @Override
    public List<UserStatus> readAll() {
        return data.values().stream().toList();
    }

    @Override
    public UserStatus readByUserId(UUID userId) {
        return data.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        data.values().removeIf(userStatus -> userStatus.getUserId().equals(userId));
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return data.values().stream()
                .anyMatch(userStatus -> userStatus.getUserId().equals(userId));
    }
}
package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true
)
public class JCFUserStatusRepository implements UserStatusRepository {

    private final Map<UUID, UserStatus> userStatuses
            = new ConcurrentHashMap<>();

    @Override
    public void save(UserStatus userStatus) {
        userStatuses.put(
                userStatus.getUserId(),
                userStatus
        );
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return userStatuses.values()
                .stream()
                .filter(userStatus ->
                        userStatus.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return Optional.ofNullable(
                userStatuses.get(userId)
        );
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(
                userStatuses.values()
        );
    }

    @Override
    public void deleteById(UUID id) {
        userStatuses.entrySet()
                .removeIf(entry ->
                        entry.getValue().getId().equals(id));
    }

    @Override
    public void deleteByUserId(UUID userId) {
        userStatuses.remove(userId);
    }
}
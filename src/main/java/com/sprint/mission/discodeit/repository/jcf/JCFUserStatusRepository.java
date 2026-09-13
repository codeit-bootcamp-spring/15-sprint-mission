package com.sprint.mission.discodeit.repository.jcf;
import org.springframework.stereotype.Repository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.*;
@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> data = new HashMap<>();
    @Override
    public UserStatus save(UserStatus entity) {
        data.put(entity.getId(), entity);
        return entity;
    }
    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }
    @Override
    public List<UserStatus> findAll() {
        return List.copyOf(data.values());
    }
    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
    public Optional<UserStatus> findByUserId(UUID userId) {
        for (UserStatus status : findAll()) {
            if (status.getUserId().equals(userId)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}

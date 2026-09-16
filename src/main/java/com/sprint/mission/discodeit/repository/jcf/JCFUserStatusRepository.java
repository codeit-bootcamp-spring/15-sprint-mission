package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserStatusRepository implements UserStatusRepository {
    private final List<UserStatus> userStatuses;

    public JCFUserStatusRepository() {
        this.userStatuses = new ArrayList<>();
    }

    @Override
    public boolean save(UserStatus userStatus) {
        return this.userStatuses.add(userStatus);
    }

    @Override
    public UserStatus find(UUID id) {
        return this.userStatuses.stream().filter(x-> x.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public UserStatus findByUserId(UUID userid) {
        return this.userStatuses.stream().filter(x-> x.getUserId().equals(userid)).findFirst().orElse(null);
    }

    @Override
    public List<UserStatus> findAll() {
        return this.userStatuses.stream().toList();
    }

    @Override
    public boolean delete(UUID id) {
        return this.userStatuses.removeIf(x-> x.getId().equals(id));
    }
}

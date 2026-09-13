package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.*;

@Repository
@ConditionalOnProperty(
        name = "discodeit.repository.type",
        havingValue = "jcf",
        matchIfMissing = true
)
public class JCFUserStatusRepository implements UserStatusRepository {

    private final Map<UUID, UserStatus> data = new HashMap<>();

    public JCFUserStatusRepository(){
    }
    @Override
    public UserStatus save(UserStatus userStatus) {
        data.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<UserStatus> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return data.values().stream().filter(userStatus -> userStatus.getUserId().equals(userId)).findFirst();
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {

        UserStatus userStatus = findByUserId(userId).orElseThrow(() -> new NoSuchElementException("삭제할 userid의 스테이터스 없음 : "+ userId));

        deleteById(userStatus.getId());

        /*if(!existsByUserId(userId)){
            throw new NoSuchElementException("userId의 스테이터스가 없습니다 : "+userId);
        }
        deleteById(findByUserId(userId).get().getId());*/
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return data.values().stream().anyMatch(userStatus -> userStatus.getUserId().equals(userId));
    }
}

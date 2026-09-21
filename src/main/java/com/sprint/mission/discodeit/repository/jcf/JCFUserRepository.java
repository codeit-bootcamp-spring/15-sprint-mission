package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserRepository implements UserRepository {

  private final Set<User> users;

  public JCFUserRepository() {
    this.users = new HashSet<>();
  }


  @Override
  public boolean create(User user) {
    return this.users.add(user);
  }

  @Override
  public User find(UUID userId) {
    return this.users.stream().filter(x -> x.getId().equals(userId)).findFirst().orElse(null);
  }

  @Override
  public User findByName(String name) {
    return this.users.stream().filter(x -> x.getUsername().equals(name)).findFirst().orElse(null);
  }

  @Override
  public Set<User> findAll() {
    return new HashSet<>(this.users);
  }

  @Override
  public boolean update(User user) {
    boolean isExist = this.users.removeIf(x -> x.getId().equals(user.getId()));
    return isExist && this.users.add(user);
  }

  @Override
  public void delete(UUID userId) {
    this.users.removeIf(x -> x.getId().equals(userId));
  }
}

package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf", matchIfMissing = true)
public class JCFReadStatusRepository implements ReadStatusRepository {

  private final List<ReadStatus> readStatuses;

  public JCFReadStatusRepository() {
    this.readStatuses = new ArrayList<>();
  }

  @Override
  public ReadStatus save(ReadStatus readStatus) {
    this.readStatuses.add(readStatus);
    return readStatus;
  }

  @Override
  public ReadStatus isAlreadyExist(UUID userId, UUID channelId) {
    return this.readStatuses.stream()
        .filter(x -> x.getUserId().equals(userId))
        .filter(x -> x.getChannelId().equals(channelId))
        .findFirst()
        .orElse(null);
  }

  @Override
  public ReadStatus find(UUID id) {
    return this.readStatuses.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return this.readStatuses.stream().filter(x -> x.getUserId().equals(userId)).toList();
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    return this.readStatuses.stream().filter(x -> x.getChannelId().equals(channelId)).toList();
  }

  @Override
  public void delete(UUID id) {
    this.readStatuses.removeIf(x -> x.getId().equals(id));
  }
}

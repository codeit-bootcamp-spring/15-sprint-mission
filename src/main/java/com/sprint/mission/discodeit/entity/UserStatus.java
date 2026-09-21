package com.sprint.mission.discodeit.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

public class UserStatus extends Common {

  @Getter
  private final UUID userId;
  private Instant lastOnlineAt;


  public UserStatus(UUID userId) {
    super();
    this.userId = userId;
    this.lastOnlineAt = Instant.now();
  }

  public void updateOnlineAt() {
    updateOnlineAt(Instant.now());
  }

  public void updateOnlineAt(Instant lastOnlineAt) {
    this.lastOnlineAt = lastOnlineAt;
    this.autoSetUpdatedAt();
  }

  public Instant getLastActiveAt() {
    return lastOnlineAt;
  }

  public boolean isOnline() {
    Instant fiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));
    return !lastOnlineAt.isBefore(fiveMinutesAgo);
  }

}

package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.*;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final Instant createdAt;
  private final UUID userId;
  private Instant lastActiveAt;
  private Instant updatedAt;

  public UserStatus(UUID userId, Instant lastActiveAt) {
    this.userId = userId;
    this.lastActiveAt = java.util.Objects.requireNonNull(lastActiveAt);
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  public void update() {
    this.lastActiveAt = Instant.now();
  }

  public boolean isOnline() {
    Instant now = Instant.now();
    Duration elapsed = Duration.between(lastActiveAt, now);
    // Session decision: exactly five minutes is offline.
    return !elapsed.isNegative() && elapsed.compareTo(Duration.ofMinutes(5)) < 0;
  }

}

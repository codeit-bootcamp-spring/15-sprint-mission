package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final Instant createdAt;
  private final UUID userId;
  private final UUID channelId;
  private Instant lastReadAt;
  private Instant updatedAt;

  public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
    this.userId = userId;
    this.channelId = channelId;
    this.lastReadAt = java.util.Objects.requireNonNull(lastReadAt);
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
  }

  public void update(Instant lastReadAt) {
    this.lastReadAt = Instant.now();
    updatedAt = Instant.now();
  }

}

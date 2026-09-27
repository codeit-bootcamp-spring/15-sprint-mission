package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final Instant createdAt;
  private String username;
  private String email;
  private String password;
  private UUID profileId;
  private Instant updatedAt = getCreatedAt();

  public User(String username, String email, String password, UUID profileId) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profileId = profileId;
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
  }

  public void update(String username, String email, String password) {
    if (username != null) {
      this.username = username;
    }
    if (email != null) {
      this.email = email;
    }
    if (password != null) {
      this.password = password;
    }
    updatedAt = Instant.now();
  }

  public void replaceProfile(UUID profileId) {
    this.profileId = profileId;
    updatedAt = Instant.now();
  }
}

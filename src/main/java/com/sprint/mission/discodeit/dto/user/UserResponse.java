package com.sprint.mission.discodeit.dto.user;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

  private UUID id;
  private String username;
  private String email;
  private UUID profileId;
  private Instant createdAt;
  private Instant updatedAt;
  private boolean online;
}


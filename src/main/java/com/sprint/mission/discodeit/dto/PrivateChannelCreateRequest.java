package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(@NotEmpty List<UUID> participantIds) {

  public PrivateChannelCreateRequest(List<UUID> participantIds) {
    this.participantIds = List.copyOf(participantIds);
  }
}

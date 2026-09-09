package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Common implements Serializable {

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    public Common() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    protected void updateUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}

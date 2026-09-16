package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Common implements Serializable {
    private final UUID id;
    @Setter
    private Instant createdAt;
    // 서로 의존 관계로 한번에 업데이트가 되는 경우 업데이트 시각 동기화.
    @Setter
    private Instant updatedAt;


    public Common() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void autoSetUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}

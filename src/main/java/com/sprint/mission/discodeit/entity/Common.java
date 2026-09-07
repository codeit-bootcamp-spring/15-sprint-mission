package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Common implements Serializable {
    private UUID id;
    private Instant createAt;

    public Common(UUID id){
        this.id = id;
        this.createAt = Instant.now();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Common common = (Common) o;
        return Objects.equals(id, common.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

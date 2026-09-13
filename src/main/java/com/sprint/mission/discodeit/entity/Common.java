package com.sprint.mission.discodeit.entity;
import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
@Getter
public abstract class Common implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id = UUID.randomUUID();
    private final Instant createdAt = Instant.now();
    @Override public final boolean equals(Object other) {
        return this == other || other != null && getClass() == other.getClass()                 && id.equals(((Common) other).id);
    }
    @Override public final int hashCode() {
        return id.hashCode();
    }
}

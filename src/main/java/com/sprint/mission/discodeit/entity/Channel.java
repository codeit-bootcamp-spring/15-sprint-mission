package com.sprint.mission.discodeit.entity;
import lombok.Getter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Getter
public class Channel extends Common {
    private final String type;
    private final List<UUID> userIds = new ArrayList<>();
    private String name;
    private String description;
    private Instant updatedAt = getCreatedAt();
    public Channel(String type, String name, String description) {
        this.type = java.util.Objects.requireNonNull(type);
        this.name = "PUBLIC".equals(type) ? name : null;
        this.description = "PUBLIC".equals(type) ? description : null;
    }
    public void update(String name, String description) {
        if ("PRIVATE".equals(type)) throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        updatedAt = Instant.now();
    }
    public List<UUID> getUserIds() {
        return new ArrayList<>(userIds);
    }

    public void addUser(UUID userId) {
        if (!userIds.contains(userId)) {
            userIds.add(userId);
            updatedAt = Instant.now();
        }
    }

    public void removeUser(UUID userId) {
        if (userIds.remove(userId)) {
            updatedAt = Instant.now();
        }
    }
}

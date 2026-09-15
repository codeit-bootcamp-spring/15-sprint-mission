package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Message implements Serializable {
    private static final long seriaVersionUID = 1L;

    private UUID id;
    private UUID userid;
    private UUID ChannelId;
    private String content;
    private Instant createdAt;
    private Instant  updateAt;

    // 좋아요를 누른 사용자 ID
    private final Set<UUID> likeUserIds = new HashSet<>();
    public Set<UUID> getLikeUserIds() {
        return likeUserIds;
    }

    public Message(UUID userid , UUID channelId, String content) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userid = userid;
        this.ChannelId = channelId;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserid() {
        return userid;
    }

    public UUID getChannelId() {
        return ChannelId;
    }

    public String getContent() {
        return content;
    }

    public Instant  getCreatedAt() {
        return createdAt;
    }


    public Instant  getUpdateAt() {
        return updateAt;
    }

    public void update(String content) {
    }

    public void setContent(String content) {
    }
}

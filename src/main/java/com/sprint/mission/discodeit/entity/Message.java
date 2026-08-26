package com.sprint.mission.discodeit.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Message {

    private UUID id;
    private UUID userid;
    private UUID ChannelId;
    private String content;
    private Long createdAt;
    private Long updateAt;

    // 좋아요를 누른 사용자 ID
    private final Set<UUID> likeUserIds = new HashSet<>();
    public Set<UUID> getLikeUserIds() {
        return likeUserIds;
    }

    public Message(UUID userid , UUID channelId, String content) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
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

    public Long getCreatedAt() {
        return createdAt;
    }


    public Long getUpdateAt() {
        return updateAt;
    }

    public void update(String content) {
        this.content = content;
        this.updateAt =System.currentTimeMillis();
    }

}

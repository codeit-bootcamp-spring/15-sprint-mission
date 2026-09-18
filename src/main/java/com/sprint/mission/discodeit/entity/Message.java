package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String content;
    private final UUID authorId;
    private final UUID channelId;
    private final List<UUID> attachmentIds; // 첨부파일 ID 목록

    // 기존 호환용 생성자 (첨부파일 없는 경우)
    public Message(String content, UUID authorId, UUID channelId) {
        this(content, authorId, channelId, new ArrayList<>());
    }

    // 첨부파일 포함 생성자
    public Message(String content, UUID authorId, UUID channelId, List<UUID> attachmentIds) {
        super();
        this.content = content;
        this.authorId = authorId;
        this.channelId = channelId;
        this.attachmentIds = attachmentIds != null ? attachmentIds : new ArrayList<>();
    }

    public void update(String content) {
        this.content = content;
        touch();
    }
}
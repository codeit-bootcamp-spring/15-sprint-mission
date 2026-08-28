package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    private UUID id;
    private Long createdAt; // 객채 생성 시간
    private Long updatedAt; // 객체 수정 시간 -> 둘 다 유닉스 타임스탬프로 나타내기위함
    private String content;

    /// Jackson 복원용 기본 생성자
    public Message() {
    }

    public Message(String content) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getContent() {
        return content;
    }


    public Long updateUpdatedAt(Long updatedAt) {
        return this.updatedAt = updatedAt;
    }

    public String updateContent(String content) {
        return this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}' + "\n";
    }
}

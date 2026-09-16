package com.sprint.mission.discodeit.entity;


import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public final class BinaryContent implements Serializable {

    private final UUID id;
    private final Instant createdAt;
    private final String contentType;
    private final String fileName;
    private final long fileSize;
    private final byte[] content;

    public BinaryContent(
            String contentType,
            String fileName,
            long fileSize,
            byte[] content
    ) {

        if (content == null) {
            throw new IllegalArgumentException(
                    "파일 내용은 null일 수 없습니다."
            );
        }

        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.contentType = contentType;
        this.fileName = fileName;
        this.fileSize = fileSize;

        // 중요
        this.content = content.clone();
    }

    // byte[]만 직접 getter 작성
    public byte[] getContent() {
        return content.clone();
    }
}
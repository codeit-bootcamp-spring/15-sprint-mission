package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {

    private final UUID id;
    private final Instant createdAt;
    private final String fileName;
    private final Long fileSize;
    private final String fileType;

    public BinaryContent(String fileName, Long fileSize, String fileType) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
    }
}
